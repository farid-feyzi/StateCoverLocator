/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk7.com.sun.source.tree;

import jdk7.javax.lang.model.element.Name;

/**
 * A tree node for a 'continue' statement.
 *
 * For example:
 * <pre>
 *   continue;
 *   continue <em>label</em> ;
 * </pre>
 *
 * @jls section 14.16
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public interface ContinueTree extends StatementTree {
    Name getLabel();
}
