import time
import pandas as pd
import pickle
import xgboost as xgb
import os
import gc
import logging
from sklearn import metrics

from sklearn.model_selection import train_test_split

from pca.PCAForXgb import PCAForXgb
from pca.PCAColumn import PCAColumn
from Utils.config import Configure
from Utils.string_utils import preprocess_numbers

class VarWithPCA(PCAForXgb):

    def prepreocess(self, ori_file_data, all_pca_added_df, misson_tp, is_training=False):
        """
        :param ori_file_data: DataFrame
        :param all_pca_added_df: DataFrame
        :return X: matrix of features, Y: labels
        """
        assert ori_file_data.shape[0] == all_pca_added_df.shape[0]

        ori_file_data.drop(['id', 'line', 'column'], axis=1, inplace=True)
        Y = ori_file_data['putin']
        ori_file_data.drop(['putin'], axis=1,inplace=True)
        X = pd.concat([ori_file_data, all_pca_added_df], axis=1)

        columns = [i for i in X.columns.tolist() ]

        if misson_tp == 'v0':
            label_encoded_cols = ['filename','tdname','methodname',
                'allloc','allloctp','allfld','allfldtp','bodyctl','befsyn','bdsyn','afsyn',
                'bes0','bes1','bes2','bes3','bes4','bes5','bds0','bds1','bds2','bds3','afs0','afs1',
                'afs2','afs3','lv0','lv1','lv2','lv3','befcd','befpre','pred',
                'roottp','varname','vartype','ltt0', 'ltt1', 'ltt2','wd0', 'wd1', 'wd2', 'twdl0',
                'lastassign','bodyuse','outuse','lastpre',
                'docexcp', 'docop',
                'varpre1st', 'varpre2nd', 'tppre1st', 'tppre2nd', 'tppre3rd',
                'pstmt0','pstmt1','nstmt0','nstmt1']

        elif misson_tp == 'var':
            label_encoded_cols = ['filename', 'tdname', 'methodname',
                'allloc', 'allloctp', 'allfld', 'allfldtp', 'bodyctl', 'befsyn', 'bdsyn', 'afsyn',
                'bes0', 'bes1', 'bes2', 'bes3', 'bes4', 'bes5', 'bds0', 'bds1', 'bds2', 'bds3',
                'afs0', 'afs1',
                'afs2', 'afs3', 'lv0', 'lv1', 'lv2', 'lv3', 'befcd', 'befpre', 'pred',
                'roottp', 'varname', 'vartype', 'ltt0', 'ltt1', 'ltt2', 'wd0', 'wd1', 'wd2', 'twdl0',
                'lastassign', 'bodyuse', 'outuse', 'lastpre',
                'docexcp', 'docop',
                'varpre1st', 'varpre2nd', 'tppre1st', 'tppre2nd', 'tppre3rd',
                'pstmt0','pstmt1','nstmt0','nstmt1', 'roottp', 'ariop', 'mtd0']

        if misson_tp == 'var':
            X['num0'] = X['num0'].apply(preprocess_numbers)
            X['num1'] = X['num1'].apply(preprocess_numbers)

        #for training
        if is_training:
            all_encoders = {}   #TODO: reusing the encoders of Expr
            for col in columns:
                if str(col) in label_encoded_cols:
                    all_encoders[col] = {}
                    X[col] = self.encoder_column(X[col], all_encoders[col])

            #dump all label encoders
            with open(self.__configure__.get_all_label_encoders(misson_tp), 'wb') as f:
                pickle.dump(all_encoders, f, protocol=2)

        #for predicting
        else:
            with open(self.__configure__.get_all_label_encoders(misson_tp), 'r') as f:
                all_encoders = pickle.load(f)
            Y = None
            for col in columns:
                if str(col) in label_encoded_cols:
                    X[col] = self.encoder_column(X[col], all_encoders[col])

        return X, Y

    def get_predicated_label(self, y_pred):
        result = []
        for item in y_pred:
            if item >= 0.5:
                result.append(True)
            else:
                result.append(False)

        # print result
        return result


    def train(self, is_v0):
        pca_start_time =  time.time()

        if is_v0:
            logging.info('>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> START V0 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>')
            var_file = self.__configure__.get_raw_v0_train_in_file()
            misson_tp = 'v0'
            model_file = self.__configure__.get_v0_model_file()
            transformed_tags = Configure.get_v0_tags_for_pca()

        else:
            logging.info('>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> START ALLVAR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>')
            misson_tp = 'var'
            var_file = self.__configure__.get_raw_var_train_in_file()
            model_file = self.__configure__.get_var_model_file()
            transformed_tags = Configure.get_var_tags_for_pca()

        ori_file_data = pd.read_csv(var_file, sep='\t', header=0, encoding='utf-8')

        pd.set_option('display.max_columns', None)

        #logging.info(ori_file_data.describe(include='all'))

        all_pca_added_df = PCAColumn.get_pca_dataframe(ori_file_data,
                                                       self.__configure__,
                                                       misson_tp,
                                                       transformed_tags,
                                                       is_training=True)
        pca_end_time = time.time()
        logging.info('VAR TRAINING PCA TIME: {} s'.format(pca_end_time - pca_start_time))

        #label encoder
        X, Y = self.prepreocess(ori_file_data, all_pca_added_df, misson_tp, is_training=True)
        encoder_end_time = time.time()
        logging.info('VAR TRAINING ENCODER TIME: {} s'.format(encoder_end_time - pca_end_time))

        X_train, X_valid, y_train, y_valid = train_test_split(X, Y, test_size=0.1, random_state=7)

        gc.collect()

        if is_v0:
            logging.info('V0 TRAINING SET SIZE: {}'.format(X_train.shape))
            logging.info('V0 VALIDATION SET SIZE: {}'.format(X_valid.shape))
        else:
            logging.info('ALLVAR TRAINING SET SIZE: {}'.format(X_train.shape))
            logging.info('ALLVAR VALIDATION SET SIZE: {}'.format(X_valid.shape))

        # BINARY-CLASSIFICATION PROBLEM FOR VAR

        M_train = xgb.DMatrix(X_train, label=y_train)
        M_valid = xgb.DMatrix(X_valid, label=y_valid)

        params = {
            'booster': 'gbtree',
            'objective': 'binary:logistic',
            'max_depth': 6,
            'eta': 0.1,
            'gamma': 0.2,
            'subsample': 0.7,
            'col_sample_bytree': 0.2,
            'min_child_weight': 1,
            'save_period': 0,
            'eval_metric': 'error',
            'silent': 1,
            'lambda': 2
        }
        num_round = 1000
        early_stop = 50
        learning_rates = [(num_round - i) / (num_round * 5.0) for i in range(num_round)]

        watchlist = [(M_train, 'train'), (M_valid, 'eval')]
        model = xgb.train(params, M_train, num_boost_round=num_round, evals=watchlist,
                          early_stopping_rounds=early_stop, learning_rates=learning_rates)

        with open(model_file, 'wb') as f:
            pickle.dump(model, f, protocol=2)
            print('VAR MODEL SAVED AS {}'.format(model_file))

        train_end_time = time.time()
        logging.info("Var best score: {}".format(model.best_score))
        logging.info("VAR XGB Training Time {} s".format(train_end_time - pca_end_time))

        y_pred = model.predict(M_valid)
        y_pred_label = self.get_predicated_label(y_pred)
        self.get_matrics(y_valid, y_pred_label)

    def get_matrics(self, y_true, y_pred):
        classify_report = metrics.classification_report(y_true, y_pred)
        logging.info(classify_report)


    def predict(self, is_v0):

        if is_v0:
            misson_tp = 'v0'
            data_file_path = self.__configure__.get_raw_v0_pred_in_file()
            transformed_tags = Configure.get_v0_tags_for_pca()
            model_file = self.__configure__.get_v0_model_file()
            var_predicted = self.__configure__.get_v0_pred_out_file()
        else:
            misson_tp = 'var'
            data_file_path = self.__configure__.get_raw_var_pred_in_file()
            transformed_tags = Configure.get_var_tags_for_pca()
            model_file = self.__configure__.get_var_model_file()
            var_predicted = self.__configure__.get_var_pred_out_file()


        data = pd.read_csv(data_file_path, sep='\t', header=0, encoding='utf-8')

        all_var_name = data['varname'].copy()
        all_isfld = data['isfld'].copy()

        pca_df = PCAColumn.get_pca_dataframe(data, self.__configure__,
                                             misson_tp,
                                             transformed_tags,
                                             is_training=False)

        X, nop_Y = self.prepreocess(data, pca_df, misson_tp, False)
        xgb_var_model = pickle.load(open(model_file, 'r'))

        M_pred = xgb.DMatrix(X)
        y_prob = xgb_var_model.predict(M_pred)

        if os.path.exists(var_predicted):
            os.remove(var_predicted)

        with open(var_predicted, 'w') as output:
            for i in range(y_prob.shape[0]):
                # print all_var_name[i], '\t', y_prob[i]
                if all_isfld[i]:
                    output.write('{}'.format(all_var_name[i]) + "#F")  #is field
                else:
                    output.write('{}'.format(all_var_name[i]))  # predicate
                output.write('\t%.17f' % y_prob[i])
                output.write('\n')
