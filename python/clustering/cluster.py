import pandas as pd
import numpy as np
from math import isnan
from sklearn.cluster import KMeans
from sklearn.cluster import AgglomerativeClustering
import os
from Utils.config import *
from sklearn.externals import joblib

class Cluster(object):

	def __init__(self, configure):
		self.__configure__ = configure

	@staticmethod
	def var_to_vec(v):
		result = np.array(np.zeros(27 * 27 + 1))
		if len(v) == 1:
			result[26 * 26] = 1
		else:
			for j in range(0, len(v) - 1):
				first = ord(v[j]) - ord('a')
				second = ord(v[j + 1]) - ord('a')
				if first < 0 or first >= 26:
					first = 26
				if second < 0 or second >= 26:
					second = 26
				# print(first)
				# print(second)
				pos = first * 27 + second
				# print(pos)
				result[pos] = 1
		return result

	def split_and_lower(self, s):
		result = list()

		current = ''
		for cur in s:
			i = ord(cur)
			if i >= ord('A') and i <= ord('Z'):
				if (len(current) > 0):
					result.append(current.lower())
				current = ''
			current += cur

		if (len(current) > 0):
			result.append(current.lower())
		return result

	def name_to_vec(self, name, mapping):
		result = np.array(np.zeros(len(mapping)))
		words = self.split_and_lower(name.split('.', 1)[0])
		for w in words:
			i = mapping[w]
			result[i] = 1
		return result

	def cluster_string(self):
		# config file path
		var_file_path = self.__configure__.get_raw_var_train_in_file()
		expr_file_path = self.__configure__.get_raw_expr_train_in_file()

		# read data
		var_data = pd.read_csv(var_file_path, sep='\t', header=0, encoding='utf-8')
		expr_data = pd.read_csv(expr_file_path, sep='\t', header=0, encoding='utf-8')

		var_dataset = var_data.values
		expr_dataset = expr_data.values

		var_encoder = self.cluster_var(var_dataset, expr_dataset)
		func_encoder = self.cluster_func(var_dataset, expr_dataset)
		file_encoder = self.cluster_file(var_dataset, expr_dataset)

		encoder = {}
		encoder['var'] = var_encoder
		encoder['func'] = func_encoder
		encoder['file'] = file_encoder

		return encoder, var_data.shape[1], expr_data.shape[1]

	def cluster_var(self, var_dataset, expr_dataset):
		all_var = np.row_stack((var_dataset[:, 5:6], expr_dataset[:, 5:6]))

		# all_var = np.array([['len'], ['length'], ['cross'], ['clockwise']])
		all_var = all_var.astype(str)

		unique_var = set()

		for i in range(0, all_var.shape[0]):
			unique_var.add(str(all_var[i][0]).lower())

		X = np.mat(np.zeros((len(unique_var), 27 * 27 + 1)))
		varnames = list()

		i = 0
		for v in unique_var:
			X[i] = self.var_to_vec(v)
			varnames.append(v)
			i = i + 1

		print len(unique_var) / 20

		kmeans = KMeans(n_clusters=len(unique_var) / 20, random_state=0).fit(X)
		# ac = AgglomerativeClustering(n_clusters=len(unique_var) / 20, linkage="complete").fit(X)

		# export model
		joblib.dump(kmeans, self.__configure__.get_var_cluster_model_file())

		result = {}
		output_file_path = self.__configure__.get_var_cluster_file()
		if (os.path.exists(output_file_path)):
			os.remove(output_file_path)
		with open(output_file_path, 'w+') as f:
			for i in range(0, X.shape[0]):
				result[varnames[i]] = kmeans.labels_[i]
				f.write('%s\t' % varnames[i])
				f.write('%d\n' % kmeans.labels_[i])
		return result

	def cluster_func(self, var_dataset, expr_dataset):
		all_function = np.row_stack((var_dataset[:, 4:5], expr_dataset[:, 4:5]))

		return self.cluster_func_or_file(all_function, self.__configure__.get_func_cluster_model_file(),
			self.__configure__.get_func_cluster_file())


	def cluster_file(self, var_dataset, expr_dataset):
		all_file = np.row_stack((var_dataset[:, 3:4], expr_dataset[:, 3:4]))

		return self.cluster_func_or_file(all_file, self.__configure__.get_file_cluster_model_file(),
			self.__configure__.get_file_cluster_file())


	def cluster_func_or_file(self, all_raw_data, model_file, output_file_path):
		# all_raw_data = np.array([["getWord.java"],["setWord.java"],["crossOutput.java"],["produceOutput.java"],["InCode.java"]])
		all_data = all_raw_data.astype(str)
		unique_words = {}
		unique_f = set()

		for i in range(0, all_data.shape[0]):
			current = str(all_data[i][0])
			unique_f.add(current)
			words = self.split_and_lower(current.split('.', 1)[0])
			for w in words:
				if not (w in unique_words):
					unique_words[w] = len(unique_words)

		X = np.mat(np.zeros((len(unique_f), len(unique_words))))
		fnames = list()

		i = 0
		for f in unique_f:
			X[i] = self.name_to_vec(f, unique_words)
			fnames.append(f)
			i = i + 1

		kmeans = KMeans(n_clusters=len(unique_f) / 20, random_state = 0).fit(X)

		joblib.dump(kmeans, model_file)

		result = {}
		if (os.path.exists(output_file_path)):
			os.remove(output_file_path)
		with open(output_file_path, 'w+') as f:
			for i in range(0, X.shape[0]):
				result[fnames[i]] = kmeans.labels_[i]
				f.write('%s\t' % fnames[i])
				f.write('%d\n' % kmeans.labels_[i])

		return result

	def get_cluster(self):
		encoder = {}
		model = {}
		encoder['var'], model['var'] = self.get_var_cluster()
		encoder['func'], model['func'] = self.get_func_cluster()
		encoder['file'], model['file'] = self.get_file_cluster()
		return encoder, model

	def get_cluster_routine(self, model_file, cluster_file_path):
		result = {}
		data = pd.read_csv(cluster_file_path, sep='\t', header=None, encoding='utf-8')
		dataset = data.values
		for i in range(0, dataset.shape[0]):
			result[str(dataset[i, 0])] = dataset[i, 1]

		kmeans = joblib.load(model_file)
		return result, kmeans

	def get_var_cluster(self):
		return self.get_cluster_routine(self.__configure__.get_var_cluster_model_file(), self.__configure__.get_var_cluster_file())

	def get_func_cluster(self):
		return self.get_cluster_routine(self.__configure__.get_func_cluster_model_file(), self.__configure__.get_func_cluster_file())

	def get_file_cluster(self):
		return self.get_cluster_routine(self.__configure__.get_file_cluster_model_file(), self.__configure__.get_file_cluster_file())