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

package javafx.stage;

/**
Builder class for javafx.stage.PopupWindow
@see javafx.stage.PopupWindow
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public abstract class PopupWindowBuilder<B extends javafx.stage.PopupWindowBuilder<B>> extends javafx.stage.WindowBuilder<B> {
    protected PopupWindowBuilder() {
    }
    
    
    private int __set;
    public void applyTo(javafx.stage.PopupWindow x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.setAutoFix(this.autoFix);
        if ((set & (1 << 1)) != 0) x.setAutoHide(this.autoHide);
        if ((set & (1 << 2)) != 0) x.setConsumeAutoHidingEvents(this.consumeAutoHidingEvents);
        if ((set & (1 << 3)) != 0) x.setHideOnEscape(this.hideOnEscape);
        if ((set & (1 << 4)) != 0) x.setOnAutoHide(this.onAutoHide);
    }
    
    private boolean autoFix;
    /**
    Set the value of the {@link javafx.stage.PopupWindow#isAutoFix() autoFix} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B autoFix(boolean x) {
        this.autoFix = x;
        __set |= 1 << 0;
        return (B) this;
    }
    
    private boolean autoHide;
    /**
    Set the value of the {@link javafx.stage.PopupWindow#isAutoHide() autoHide} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B autoHide(boolean x) {
        this.autoHide = x;
        __set |= 1 << 1;
        return (B) this;
    }
    
    private boolean consumeAutoHidingEvents;
    /**
    Set the value of the {@link javafx.stage.PopupWindow#getConsumeAutoHidingEvents() consumeAutoHidingEvents} property for the instance constructed by this builder.
    * @since JavaFX 2.2
    */
    @SuppressWarnings("unchecked")
    public B consumeAutoHidingEvents(boolean x) {
        this.consumeAutoHidingEvents = x;
        __set |= 1 << 2;
        return (B) this;
    }
    
    private boolean hideOnEscape;
    /**
    Set the value of the {@link javafx.stage.PopupWindow#isHideOnEscape() hideOnEscape} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B hideOnEscape(boolean x) {
        this.hideOnEscape = x;
        __set |= 1 << 3;
        return (B) this;
    }
    
    private javafx.event.EventHandler<javafx.event.Event> onAutoHide;
    /**
    Set the value of the {@link javafx.stage.PopupWindow#getOnAutoHide() onAutoHide} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B onAutoHide(javafx.event.EventHandler<javafx.event.Event> x) {
        this.onAutoHide = x;
        __set |= 1 << 4;
        return (B) this;
    }
    
}
