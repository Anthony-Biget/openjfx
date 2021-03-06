/* 
 * Copyright (c) 2011, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.scene.control;

/**
Builder class for javafx.scene.control.TextField
@see javafx.scene.control.TextField
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class TextFieldBuilder<B extends javafx.scene.control.TextFieldBuilder<B>> extends javafx.scene.control.TextInputControlBuilder<B> implements javafx.util.Builder<javafx.scene.control.TextField> {
    protected TextFieldBuilder() {
    }
    
    /** Creates a new instance of TextFieldBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.scene.control.TextFieldBuilder<?> create() {
        return new javafx.scene.control.TextFieldBuilder();
    }
    
    private int __set;
    public void applyTo(javafx.scene.control.TextField x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.setAlignment(this.alignment);
        if ((set & (1 << 1)) != 0) x.setOnAction(this.onAction);
        if ((set & (1 << 2)) != 0) x.setPrefColumnCount(this.prefColumnCount);
        if ((set & (1 << 3)) != 0) x.setPromptText(this.promptText);
    }
    
    private javafx.geometry.Pos alignment;
    /**
    Set the value of the {@link javafx.scene.control.TextField#getAlignment() alignment} property for the instance constructed by this builder.
    * @since JavaFX 2.1
    */
    @SuppressWarnings("unchecked")
    public B alignment(javafx.geometry.Pos x) {
        this.alignment = x;
        __set |= 1 << 0;
        return (B) this;
    }
    
    private javafx.event.EventHandler<javafx.event.ActionEvent> onAction;
    /**
    Set the value of the {@link javafx.scene.control.TextField#getOnAction() onAction} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B onAction(javafx.event.EventHandler<javafx.event.ActionEvent> x) {
        this.onAction = x;
        __set |= 1 << 1;
        return (B) this;
    }
    
    private int prefColumnCount;
    /**
    Set the value of the {@link javafx.scene.control.TextField#getPrefColumnCount() prefColumnCount} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B prefColumnCount(int x) {
        this.prefColumnCount = x;
        __set |= 1 << 2;
        return (B) this;
    }
    
    private java.lang.String promptText;
    /**
    Set the value of the {@link javafx.scene.control.TextField#getPromptText() promptText} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B promptText(java.lang.String x) {
        this.promptText = x;
        __set |= 1 << 3;
        return (B) this;
    }
    
    /**
    Make an instance of {@link javafx.scene.control.TextField} based on the properties set on this builder.
    */
    public javafx.scene.control.TextField build() {
        javafx.scene.control.TextField x = new javafx.scene.control.TextField();
        applyTo(x);
        return x;
    }
}
