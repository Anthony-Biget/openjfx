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

import static com.sun.javafx.scene.control.infrastructure.ControlTestUtils.assertStyleClassContains;
import static javafx.scene.control.TreeTableColumn.SortType.ASCENDING;
import static javafx.scene.control.TreeTableColumn.SortType.DESCENDING;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.sun.javafx.scene.control.infrastructure.KeyEventFirer;
import com.sun.javafx.scene.control.infrastructure.MouseEventFirer;
import com.sun.javafx.scene.control.skin.TreeTableCellSkin;
import com.sun.javafx.scene.control.test.Data;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableView.TreeTableViewFocusModel;
import javafx.scene.control.cell.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.javafx.scene.control.TableColumnComparatorBase.TreeTableColumnComparator;
import com.sun.javafx.scene.control.infrastructure.StageLoader;
import com.sun.javafx.scene.control.infrastructure.VirtualFlowTestUtils;
import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import com.sun.javafx.scene.control.test.Person;
import com.sun.javafx.scene.control.test.RT_22463_Person;
import com.sun.javafx.tk.Toolkit;

public class TreeTableViewTest {
    private TreeTableView<String> treeTableView;
    private TreeTableView.TreeTableViewSelectionModel sm;
    private TreeTableViewFocusModel<String> fm;
    
    
    // sample data #1
    private TreeItem<String> root;
    private TreeItem<String> child1;
    private TreeItem<String> child2;
    private TreeItem<String> child3;
    
    // sample data #1
    private TreeItem<String> myCompanyRootNode;
        private TreeItem<String> salesDepartment;
            private TreeItem<String> ethanWilliams;
            private TreeItem<String> emmaJones;
            private TreeItem<String> michaelBrown;
            private TreeItem<String> annaBlack;
            private TreeItem<String> rodgerYork;
            private TreeItem<String> susanCollins;

        private TreeItem<String> itSupport;
            private TreeItem<String> mikeGraham;
            private TreeItem<String> judyMayer;
            private TreeItem<String> gregorySmith;
    
    @Before public void setup() {
        treeTableView = new TreeTableView<String>();
        sm = treeTableView.getSelectionModel();
        fm = treeTableView.getFocusModel();
        
        // build sample data #2, even though it may not be used...
        myCompanyRootNode = new TreeItem<String>("MyCompany Human Resources");
        salesDepartment = new TreeItem<String>("Sales Department");
            ethanWilliams = new TreeItem<String>("Ethan Williams");
            emmaJones = new TreeItem<String>("Emma Jones");
            michaelBrown = new TreeItem<String>("Michael Brown");
            annaBlack = new TreeItem<String>("Anna Black");
            rodgerYork = new TreeItem<String>("Rodger York");
            susanCollins = new TreeItem<String>("Susan Collins");

        itSupport = new TreeItem<String>("IT Support");
            mikeGraham = new TreeItem<String>("Mike Graham");
            judyMayer = new TreeItem<String>("Judy Mayer");
            gregorySmith = new TreeItem<String>("Gregory Smith");
            
        myCompanyRootNode.getChildren().setAll(
            salesDepartment,
            itSupport
        );
        salesDepartment.getChildren().setAll(
            ethanWilliams,
            emmaJones,
            michaelBrown, 
            annaBlack,
            rodgerYork,
            susanCollins
        );
        itSupport.getChildren().setAll(
            mikeGraham,
            judyMayer,
            gregorySmith
        );
    }

    private void installChildren() {
        root = new TreeItem<String>("Root");
        child1 = new TreeItem<String>("Child 1");
        child2 = new TreeItem<String>("Child 2");
        child3 = new TreeItem<String>("Child 3");
        root.setExpanded(true);
        root.getChildren().setAll(child1, child2, child3);
        treeTableView.setRoot(root);
    }
    
    private String debug() {
        StringBuilder sb = new StringBuilder("Selected Indices: [");
        
        List<Integer> indices = sm.getSelectedIndices();
        for (Integer index : indices) {
            sb.append(index);
            sb.append(", ");
        }
        
        sb.append("] \nFocus: " + fm.getFocusedIndex());
//        sb.append(" \nAnchor: " + getAnchor());
        return sb.toString();
    }
    
    @Test public void ensureCorrectInitialState() {
        installChildren();
        assertEquals(0, treeTableView.getRow(root));
        assertEquals(1, treeTableView.getRow(child1));
        assertEquals(2, treeTableView.getRow(child2));
        assertEquals(3, treeTableView.getRow(child3));
    }
    
    
    
    
    
    
    
    
    /***************************************************************************
     * 
     * 
     * Tests taken from TableViewTest
     * (scroll down further for the TreeViewTests)
     * 
     * 
     **************************************************************************/   

    /*********************************************************************
     * Tests for the constructors                                        *
     ********************************************************************/

    @Test public void noArgConstructorSetsNonNullSelectionModel() {
        assertNotNull(sm);
    }

    @Test public void noArgConstructor_selectedItemIsNull() {
        assertNull(sm.getSelectedItem());
    }

    @Test public void noArgConstructor_selectedIndexIsNegativeOne() {
        assertEquals(-1, sm.getSelectedIndex());
    }
    
    @Test public void noArgConstructorSetsNonNullSortPolicy() {
        assertNotNull(treeTableView.getSortPolicy());
    }
    
    @Test public void noArgConstructorSetsNullComparator() {
        assertNull(treeTableView.getComparator());
    }
    
    @Test public void noArgConstructorSetsNullOnSort() {
        assertNull(treeTableView.getOnSort());
    }

//    @Test public void singleArgConstructorSetsNonNullSelectionModel() {
//        final TreeTableView<String> b2 = new TreeTableView<String>(FXCollections.observableArrayList("Hi"));
//        assertNotNull(b2.getSelectionModel());
//    }
//
//    @Test public void singleArgConstructorAllowsNullItems() {
//        final TreeTableView<String> b2 = new TreeTableView<String>(null);
//        assertNull(b2.getItems());
//    }
//
//    @Test public void singleArgConstructorTakesItems() {
//        ObservableList<String> items = FXCollections.observableArrayList("Hi");
//        final TreeTableView<String> b2 = new TreeTableView<String>(items);
//        assertSame(items, b2.getItems());
//    }
//
//    @Test public void singleArgConstructor_selectedItemIsNull() {
//        final TreeTableView<String> b2 = new TreeTableView<String>(FXCollections.observableArrayList("Hi"));
//        assertNull(b2.getSelectionModel().getSelectedItem());
//    }
//
//    @Test public void singleArgConstructor_selectedIndexIsNegativeOne() {
//        final TreeTableView<String> b2 = new TreeTableView<String>(FXCollections.observableArrayList("Hi"));
//        assertEquals(-1, b2.getSelectionModel().getSelectedIndex());
//    }

    /*********************************************************************
     * Tests for columns                                                 *
     ********************************************************************/

    @Test public void testColumns() {
        TreeTableColumn col1 = new TreeTableColumn();

        assertNotNull(treeTableView.getColumns());
        assertEquals(0, treeTableView.getColumns().size());

        treeTableView.getColumns().add(col1);
        assertEquals(1, treeTableView.getColumns().size());

        treeTableView.getColumns().remove(col1);
        assertEquals(0, treeTableView.getColumns().size());
    }

    @Test public void testVisibleLeafColumns() {
        TreeTableColumn col1 = new TreeTableColumn();

        assertNotNull(treeTableView.getColumns());
        assertEquals(0, treeTableView.getColumns().size());

        treeTableView.getColumns().add(col1);
        assertEquals(1, treeTableView.getVisibleLeafColumns().size());

        treeTableView.getColumns().remove(col1);
        assertEquals(0, treeTableView.getVisibleLeafColumns().size());
    }
    
    @Test public void testSortOrderCleanup() {
        TreeTableView treeTableView = new TreeTableView();
        TreeTableColumn<String,String> first = new TreeTableColumn<String,String>("first");
        first.setCellValueFactory(new PropertyValueFactory("firstName"));
        TreeTableColumn<String,String> second = new TreeTableColumn<String,String>("second");
        second.setCellValueFactory(new PropertyValueFactory("lastName"));
        treeTableView.getColumns().addAll(first, second);
        treeTableView.getSortOrder().setAll(first, second);
        treeTableView.getColumns().remove(first);
        assertFalse(treeTableView.getSortOrder().contains(first));
    } 
    
    
    /*********************************************************************
     * Tests for new sorting API in JavaFX 8.0                           *
     ********************************************************************/
    
    private TreeItem<String> apple, orange, banana;
    
    // TODO test for sort policies returning null
    // TODO test for changing column sortType out of order
    
    private static final Callback<TreeTableView<String>, Boolean> NO_SORT_FAILED_SORT_POLICY =
            treeTableView1 -> false;
    
    private static final Callback<TreeTableView<String>, Boolean> SORT_SUCCESS_ASCENDING_SORT_POLICY =
            treeTableView1 -> {
                if (treeTableView1.getSortOrder().isEmpty()) return true;
                FXCollections.sort(treeTableView1.getRoot().getChildren(), new Comparator<TreeItem<String>>() {
                    @Override public int compare(TreeItem<String> o1, TreeItem<String> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                });
                return true;
            };
    
    private TreeTableColumn<String, String> initSortTestStructure() {
        TreeTableColumn<String, String> col = new TreeTableColumn<String, String>("column");
        col.setSortType(ASCENDING);
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);
        
        TreeItem<String> newRoot = new TreeItem<String>("root");
        newRoot.setExpanded(true);
        newRoot.getChildren().addAll(
                apple  = new TreeItem("Apple"), 
                orange = new TreeItem("Orange"), 
                banana = new TreeItem("Banana"));
        
        treeTableView.setRoot(newRoot);
        
        return col;
    }
    
    @Ignore("This test is only valid if sort event consumption should revert changes")
    @Test public void testSortEventCanBeConsumedToStopSortOccurring_changeSortOrderList() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        treeTableView.setOnSort(event -> {
            event.consume();
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        
        // the sort order list should be returned back to its original state
        assertTrue(treeTableView.getSortOrder().isEmpty());
    }
    
    @Test public void testSortEventCanBeNotConsumedToAllowSortToOccur_changeSortOrderList() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        treeTableView.setOnSort(event -> {
            // do not consume here - this allows the sort to happen
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Ignore("This test is only valid if sort event consumption should revert changes")
    @Test public void testSortEventCanBeConsumedToStopSortOccurring_changeColumnSortType_AscendingToDescending() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        assertEquals(ASCENDING, col.getSortType());
        treeTableView.getSortOrder().add(col);
        treeTableView.setOnSort(event -> {
            event.consume();
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
        
        // when we change from ASCENDING to DESCENDING we don't expect the sort
        // to actually change (and in fact we expect the sort type to resort
        // back to being ASCENDING)
        col.setSortType(DESCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
        assertEquals(ASCENDING, col.getSortType());
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Test public void testSortEventCanBeNotConsumedToAllowSortToOccur_changeColumnSortType_AscendingToDescending() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        assertEquals(ASCENDING, col.getSortType());
        treeTableView.getSortOrder().add(col);
        treeTableView.setOnSort(event -> {
            // do not consume here - this allows the sort to happen
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
        
        col.setSortType(DESCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        assertEquals(DESCENDING, col.getSortType());
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Ignore("This test is only valid if sort event consumption should revert changes")
    @Test public void testSortEventCanBeConsumedToStopSortOccurring_changeColumnSortType_DescendingToNull() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(DESCENDING);
        assertEquals(DESCENDING, col.getSortType());
        treeTableView.getSortOrder().add(col);
        treeTableView.setOnSort(event -> {
            event.consume();
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        
        col.setSortType(null);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        assertEquals(DESCENDING, col.getSortType());
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Test public void testSortEventCanBeNotConsumedToAllowSortToOccur_changeColumnSortType_DescendingToNull() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(DESCENDING);
        assertEquals(DESCENDING, col.getSortType());
        treeTableView.getSortOrder().add(col);
        treeTableView.setOnSort(event -> {
            // do not consume here - this allows the sort to happen
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        
        col.setSortType(null);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        assertNull(col.getSortType());
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Ignore("This test is only valid if sort event consumption should revert changes")
    @Test public void testSortEventCanBeConsumedToStopSortOccurring_changeColumnSortType_NullToAscending() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(null);
        assertNull(col.getSortType());
        treeTableView.getSortOrder().add(col);
        treeTableView.setOnSort(event -> {
            event.consume();
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        
        col.setSortType(ASCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        assertNull(col.getSortType());
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Test public void testSortEventCanBeNotConsumedToAllowSortToOccur_changeColumnSortType_NullToAscending() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(null);
        assertNull(col.getSortType());
        treeTableView.getSortOrder().add(col);
        treeTableView.setOnSort(event -> {
            // do not consume here - this allows the sort to happen
        });
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        
        col.setSortType(ASCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
        assertEquals(ASCENDING, col.getSortType());
        
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Test public void testSortMethodWithNullSortPolicy() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        treeTableView.setSortPolicy(null);
        assertNull(treeTableView.getSortPolicy());
        treeTableView.sort();
    }
    
    @Test public void testChangingSortPolicyUpdatesItemsList() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(DESCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        treeTableView.setSortPolicy(SORT_SUCCESS_ASCENDING_SORT_POLICY);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
    }
    
    @Test public void testChangingSortPolicyDoesNotUpdateItemsListWhenTheSortOrderListIsEmpty() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(DESCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        
        treeTableView.setSortPolicy(SORT_SUCCESS_ASCENDING_SORT_POLICY);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
    }
    
    @Test public void testFailedSortPolicyBacksOutLastChange_sortOrderAddition() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(DESCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);
        
        treeTableView.getSortOrder().add(col);
        
        // no sort should be run (as we have a custom sort policy), and the 
        // sortOrder list should be empty as the sortPolicy failed
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        assertTrue(treeTableView.getSortOrder().isEmpty());
    }
    
    @Test public void testFailedSortPolicyBacksOutLastChange_sortOrderRemoval() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(DESCENDING);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);

        // even though we remove the column from the sort order here, because the
        // sort policy fails the items list should remain unchanged and the sort
        // order list should continue to have the column in it.
        treeTableView.getSortOrder().remove(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getSortOrder(), col);
    }
    
    @Test public void testFailedSortPolicyBacksOutLastChange_sortTypeChange_ascendingToDescending() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(ASCENDING);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
        
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);

        col.setSortType(DESCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, banana, orange);
        assertEquals(ASCENDING, col.getSortType());
    }
    
    @Test public void testFailedSortPolicyBacksOutLastChange_sortTypeChange_descendingToNull() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(DESCENDING);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);

        col.setSortType(null);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        assertEquals(DESCENDING, col.getSortType());
    }
    
    @Test public void testFailedSortPolicyBacksOutLastChange_sortTypeChange_nullToAscending() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        col.setSortType(null);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);

        col.setSortType(ASCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        assertNull(col.getSortType());
    }
    
    @Test public void testComparatorChangesInSyncWithSortOrder_1() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        assertNull(treeTableView.getComparator());
        assertTrue(treeTableView.getSortOrder().isEmpty());
        
        treeTableView.getSortOrder().add(col);
        TreeTableColumnComparator c = (TreeTableColumnComparator)treeTableView.getComparator();
        assertNotNull(c);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(c.getColumns(), col);
    }
    
    @Test public void testComparatorChangesInSyncWithSortOrder_2() {
        // same as test above
        TreeTableColumn<String, String> col = initSortTestStructure();
        assertNull(treeTableView.getComparator());
        assertTrue(treeTableView.getSortOrder().isEmpty());
        
        treeTableView.getSortOrder().add(col);
        TreeTableColumnComparator c = (TreeTableColumnComparator)treeTableView.getComparator();
        assertNotNull(c);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(c.getColumns(), col);
        
        // now remove column from sort order, and the comparator should go to
        // being null
        treeTableView.getSortOrder().remove(col);
        assertNull(treeTableView.getComparator());
    }
    
    @Test public void testFailedSortPolicyBacksOutComparatorChange_sortOrderAddition() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        final TreeTableColumnComparator oldComparator = (TreeTableColumnComparator)treeTableView.getComparator();
        
        col.setSortType(DESCENDING);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), apple, orange, banana);
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);
        
        treeTableView.getSortOrder().add(col);
        
        assertEquals(oldComparator, treeTableView.getComparator());
    }
    
    @Test public void testFailedSortPolicyBacksOutComparatorChange_sortOrderRemoval() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        TreeTableColumnComparator oldComparator = (TreeTableColumnComparator)treeTableView.getComparator();
        assertNull(oldComparator);

        col.setSortType(DESCENDING);
        treeTableView.getSortOrder().add(col);
        VirtualFlowTestUtils.assertListContainsItemsInOrder(treeTableView.getRoot().getChildren(), orange, banana, apple);
        oldComparator = (TreeTableColumnComparator)treeTableView.getComparator();
        VirtualFlowTestUtils.assertListContainsItemsInOrder(oldComparator.getColumns(), col);
        
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);
        treeTableView.getSortOrder().remove(col);
        
        assertTrue(treeTableView.getSortOrder().contains(col));
        VirtualFlowTestUtils.assertListContainsItemsInOrder(oldComparator.getColumns(), col);
    }
    
    @Test public void testFailedSortPolicyBacksOutComparatorChange_sortTypeChange() {
        TreeTableColumn<String, String> col = initSortTestStructure();
        final TreeTableColumnComparator oldComparator = (TreeTableColumnComparator)treeTableView.getComparator();
        assertNull(oldComparator);
        
        treeTableView.setSortPolicy(NO_SORT_FAILED_SORT_POLICY);
        treeTableView.getSortOrder().add(col);
        col.setSortType(ASCENDING);
        
        assertTrue(treeTableView.getSortOrder().isEmpty());
        assertNull(oldComparator);
    }
    
    
    
    /*********************************************************************
     * Tests for specific bugs                                           *
     ********************************************************************/
//    @Test public void test_rt16019() {
//        // RT-16019: NodeMemory TableView tests fail with
//        // IndexOutOfBoundsException (ObservableListWrapper.java:336)
//        TreeTableView treeTableView = new TreeTableView();
//        for (int i = 0; i < 1000; i++) {
//            treeTableView.getItems().add("data " + i);
//        }
//    }
//
//    @Test public void test_rt15793() {
//        // ListView/TableView selectedIndex is 0 although the items list is empty
//        final TreeTableView tv = new TreeTableView();
//        final ObservableList list = FXCollections.observableArrayList();
//        tv.setItems(list);
//        list.add("toto");
//        tv.getSelectionModel().select(0);
//        assertEquals(0, tv.getSelectionModel().getSelectedIndex());
//        list.remove(0);
//        assertEquals(-1, tv.getSelectionModel().getSelectedIndex());
//    }
//
//    @Test public void test_rt17522_focusShouldMoveWhenItemAddedAtFocusIndex() {
//        final TreeTableView lv = new TreeTableView();
//        FocusModel fm = lv.getFocusModel();
//        lv.getItems().add("row1");
//        fm.focus(0);
//        assertTrue(fm.isFocused(0));
//
//        lv.getItems().add(0, "row0");
//        assertTrue(fm.isFocused(1));
//    }
//
//    @Test public void test_rt17522_focusShouldMoveWhenItemAddedBeforeFocusIndex() {
//        final TreeTableView lv = new TreeTableView();
//        FocusModel fm = lv.getFocusModel();
//        lv.getItems().addAll("row1", "row2");
//        fm.focus(1);
//        assertTrue(fm.isFocused(1));
//        assertEquals("row2", fm.getFocusedItem());
//
//        lv.getItems().add(1, "row0");
//        assertTrue(fm.isFocused(2));
//        assertEquals("row2", fm.getFocusedItem());
//        assertFalse(fm.isFocused(1));
//    }
//
//    @Test public void test_rt17522_focusShouldNotMoveWhenItemAddedAfterFocusIndex() {
//        final TreeTableView lv = new TreeTableView();
//        FocusModel fm = lv.getFocusModel();
//        lv.getItems().addAll("row1");
//        fm.focus(0);
//        assertTrue(fm.isFocused(0));
//        assertEquals("row1", fm.getFocusedItem());
//
//        lv.getItems().add(1, "row2");
//        assertTrue(fm.isFocused(0));
//        assertEquals("row1", fm.getFocusedItem());
//        assertFalse(fm.isFocused(1));
//    }
//
//    @Test public void test_rt17522_focusShouldBeResetWhenFocusedItemIsRemoved() {
//        final TreeTableView lv = new TreeTableView();
//        FocusModel fm = lv.getFocusModel();
//        lv.getItems().add("row1");
//        fm.focus(0);
//        assertTrue(fm.isFocused(0));
//
//        lv.getItems().remove("row1");
//        assertTrue(fm.getFocusedIndex() == -1);
//        assertNull(fm.getFocusedItem());
//    }
//
//    @Test public void test_rt17522_focusShouldMoveWhenItemRemovedBeforeFocusIndex() {
//        final TreeTableView lv = new TreeTableView();
//        FocusModel fm = lv.getFocusModel();
//        lv.getItems().addAll("row1", "row2");
//        fm.focus(1);
//        assertTrue(fm.isFocused(1));
//        assertEquals("row2", fm.getFocusedItem());
//
//        lv.getItems().remove("row1");
//        assertTrue(fm.isFocused(0));
//        assertEquals("row2", fm.getFocusedItem());
//    }
//
//    @Test public void test_rt17522_focusShouldNotMoveWhenItemRemovedAfterFocusIndex() {
//        final TreeTableView lv = new TreeTableView();
//        FocusModel fm = lv.getFocusModel();
//        lv.getItems().addAll("row1", "row2");
//        fm.focus(0);
//        assertTrue(fm.isFocused(0));
//        assertEquals("row1", fm.getFocusedItem());
//
//        lv.getItems().remove("row2");
//        assertTrue(fm.isFocused(0));
//        assertEquals("row1", fm.getFocusedItem());
//    }
//
//    @Test public void test_rt18385() {
//        treeTableView.getItems().addAll("row1", "row2", "row3");
//        sm.select(1);
//        treeTableView.getItems().add("Another Row");
//        assertEquals(1, sm.getSelectedIndices().size());
//        assertEquals(1, sm.getSelectedItems().size());
//        assertEquals(1, sm.getSelectedCells().size());
//    }
    
    @Test public void test_rt18339_onlyEditWhenTableViewIsEditable_tableEditableIsFalse_columnEditableIsFalse() {
        TreeTableColumn<String,String> first = new TreeTableColumn<String,String>("first");
        first.setEditable(false);
        treeTableView.getColumns().add(first);
        treeTableView.setEditable(false);
        treeTableView.edit(1, first);
        assertEquals(null, treeTableView.getEditingCell());
    }
    
    @Test public void test_rt18339_onlyEditWhenTableViewIsEditable_tableEditableIsFalse_columnEditableIsTrue() {
        TreeTableColumn<String,String> first = new TreeTableColumn<String,String>("first");
        first.setEditable(true);
        treeTableView.getColumns().add(first);
        treeTableView.setEditable(false);
        treeTableView.edit(1, first);
        assertEquals(null, treeTableView.getEditingCell());
    }
    
    @Test public void test_rt18339_onlyEditWhenTableViewIsEditable_tableEditableIsTrue_columnEditableIsFalse() {
        TreeTableColumn<String,String> first = new TreeTableColumn<String,String>("first");
        first.setEditable(false);
        treeTableView.getColumns().add(first);
        treeTableView.setEditable(true);
        treeTableView.edit(1, first);
        assertEquals(null, treeTableView.getEditingCell());
    }
    
    @Test public void test_rt18339_onlyEditWhenTableViewIsEditable_tableEditableIsTrue_columnEditableIsTrue() {
        TreeTableColumn<String,String> first = new TreeTableColumn<String,String>("first");
        first.setEditable(true);
        treeTableView.getColumns().add(first);
        treeTableView.setEditable(true);
        treeTableView.edit(1, first);
        assertEquals(new TreeTablePosition(treeTableView, 1, first), treeTableView.getEditingCell());
    }
    
//    @Test public void test_rt14451() {
//        treeTableView.getItems().addAll("Apple", "Orange", "Banana");
//        sm.setSelectionMode(SelectionMode.MULTIPLE);
//        sm.selectRange(0, 2); // select from 0 (inclusive) to 2 (exclusive)
//        assertEquals(2, sm.getSelectedIndices().size());
//    }
//    
//    @Test public void test_rt21586() {
//        treeTableView.getItems().setAll("Apple", "Orange", "Banana");
//        treeTableView.getSelectionModel().select(1);
//        assertEquals(1, treeTableView.getSelectionModel().getSelectedIndex());
//        assertEquals("Orange", treeTableView.getSelectionModel().getSelectedItem());
//        
//        treeTableView.getItems().setAll("Kiwifruit", "Pineapple", "Grape");
//        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
//        assertNull(treeTableView.getSelectionModel().getSelectedItem());
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /***************************************************************************
     * 
     * 
     * Tests taken from TreeViewTest
     * 
     * 
     **************************************************************************/   
    
    
    

    /*********************************************************************
     * Tests for the constructors                                        *
     ********************************************************************/
    
    @Test public void noArgConstructorSetsTheStyleClass() {
        assertStyleClassContains(treeTableView, "tree-table-view");
    }

    @Test public void noArgConstructorSetsNullItems() {
        assertNull(treeTableView.getRoot());
    }

    @Test public void singleArgConstructorSetsTheStyleClass() {
        final TreeTableView<String> b2 = new TreeTableView<String>(new TreeItem<String>("Hi"));
        assertStyleClassContains(b2, "tree-table-view");
    }

    /*********************************************************************
     * Tests for selection model                                         *
     ********************************************************************/

    @Test public void selectionModelCanBeNull() {
        treeTableView.setSelectionModel(null);
        assertNull(treeTableView.getSelectionModel());
    }

    @Test public void selectionModelCanBeBound() {
        TreeTableView.TreeTableViewSelectionModel<String> sm = 
                new TreeTableView.TreeTableViewArrayListSelectionModel<String>(treeTableView);
        ObjectProperty<TreeTableView.TreeTableViewSelectionModel<String>> other = 
                new SimpleObjectProperty<TreeTableView.TreeTableViewSelectionModel<String>>(sm);
        treeTableView.selectionModelProperty().bind(other);
        assertSame(sm, treeTableView.getSelectionModel());
    }

    @Test public void selectionModelCanBeChanged() {
        TreeTableView.TreeTableViewSelectionModel<String> sm = 
                new TreeTableView.TreeTableViewArrayListSelectionModel<String>(treeTableView);
        treeTableView.setSelectionModel(sm);
        assertSame(sm, treeTableView.getSelectionModel());
    }

    @Test public void canSetSelectedItemToAnItemEvenWhenThereAreNoItems() {
        TreeItem<String> element = new TreeItem<String>("I AM A CRAZY RANDOM STRING");
        treeTableView.getSelectionModel().select(element);
        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
        assertSame(element, treeTableView.getSelectionModel().getSelectedItem());
    }

    @Test public void canSetSelectedItemToAnItemNotInTheDataModel() {
        installChildren();
        TreeItem<String> element = new TreeItem<String>("I AM A CRAZY RANDOM STRING");
        treeTableView.getSelectionModel().select(element);
        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
        assertSame(element, treeTableView.getSelectionModel().getSelectedItem());
    }

    @Test public void settingTheSelectedItemToAnItemInItemsResultsInTheCorrectSelectedIndex() {
        installChildren();
        treeTableView.getSelectionModel().select(child1);
        assertEquals(1, treeTableView.getSelectionModel().getSelectedIndex());
        assertSame(child1, treeTableView.getSelectionModel().getSelectedItem());
    }

    @Ignore("Not yet supported")
    @Test public void settingTheSelectedItemToANonexistantItemAndThenSettingItemsWhichContainsItResultsInCorrectSelectedIndex() {
        treeTableView.getSelectionModel().select(child1);
        installChildren();
        assertEquals(1, treeTableView.getSelectionModel().getSelectedIndex());
        assertSame(child1, treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Ignore("Not yet supported")
    @Test public void ensureSelectionClearsWhenAllItemsAreRemoved_selectIndex0() {
        installChildren();
        treeTableView.getSelectionModel().select(0);
        treeTableView.setRoot(null);
        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
        assertEquals(null, treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Ignore("Not yet supported")
    @Test public void ensureSelectionClearsWhenAllItemsAreRemoved_selectIndex2() {
        installChildren();
        treeTableView.getSelectionModel().select(2);
        treeTableView.setRoot(null);
        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
        assertEquals(null, treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Ignore("Not yet supported")
    @Test public void ensureSelectedItemRemainsAccurateWhenItemsAreCleared() {
        installChildren();
        treeTableView.getSelectionModel().select(2);
        treeTableView.setRoot(null);
        assertNull(treeTableView.getSelectionModel().getSelectedItem());
        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
        
        TreeItem<String> newRoot = new TreeItem<String>("New Root");
        TreeItem<String> newChild1 = new TreeItem<String>("New Child 1");
        TreeItem<String> newChild2 = new TreeItem<String>("New Child 2");
        TreeItem<String> newChild3 = new TreeItem<String>("New Child 3");
        newRoot.setExpanded(true);
        newRoot.getChildren().setAll(newChild1, newChild2, newChild3);
        treeTableView.setRoot(root);
        
        treeTableView.getSelectionModel().select(2);
        assertEquals(newChild2, treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Test public void ensureSelectionIsCorrectWhenItemsChange() {
        installChildren();
        treeTableView.getSelectionModel().select(0);
        assertEquals(root, treeTableView.getSelectionModel().getSelectedItem());
        
        treeTableView.setRoot(new TreeItem<String>("New Root"));
        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
        assertEquals(null, treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Test public void ensureSelectionRemainsOnBranchWhenExpanded() {
        installChildren();
        root.setExpanded(false);
        treeTableView.getSelectionModel().select(0);
        assertTrue(treeTableView.getSelectionModel().isSelected(0));
        root.setExpanded(true);
        assertTrue(treeTableView.getSelectionModel().isSelected(0));
        assertTrue(treeTableView.getSelectionModel().getSelectedItems().contains(root));
    }
    
    /*********************************************************************
     * Tests for misc                                                    *
     ********************************************************************/
    @Test public void ensureRootIndexIsZeroWhenRootIsShowing() {
        installChildren();
        assertEquals(0, treeTableView.getRow(root));
    }
    
    @Test public void ensureRootIndexIsNegativeOneWhenRootIsNotShowing() {
        installChildren();
        treeTableView.setShowRoot(false);
        assertEquals(-1, treeTableView.getRow(root));
    }
    
    @Test public void ensureCorrectIndexWhenRootTreeItemHasParent() {
        installChildren();
        treeTableView.setRoot(child1);
        assertEquals(-1, treeTableView.getRow(root));
        assertEquals(0, treeTableView.getRow(child1));
        assertEquals(1, treeTableView.getRow(child2));
        assertEquals(2, treeTableView.getRow(child3));
    }
    
    @Test public void ensureCorrectIndexWhenRootTreeItemHasParentAndRootIsNotShowing() {
        installChildren();
        treeTableView.setRoot(child1);
        treeTableView.setShowRoot(false);
        
        // despite the fact there are children in this tree, in reality none are
        // visible as the root node has no children (only siblings), and the
        // root node is not visible.
        assertEquals(0, treeTableView.getExpandedItemCount());
        
        assertEquals(-1, treeTableView.getRow(root));
        assertEquals(-1, treeTableView.getRow(child1));
        assertEquals(-1, treeTableView.getRow(child2));
        assertEquals(-1, treeTableView.getRow(child3));
    }
    
    @Test public void ensureCorrectIndexWhenRootTreeItemIsCollapsed() {
        installChildren();
        root.setExpanded(false);
        assertEquals(0, treeTableView.getRow(root));
        
        // note that the indices are still positive, representing what the values
        // would be if this row is visible
        assertEquals(1, treeTableView.getRow(child1));
        assertEquals(2, treeTableView.getRow(child2));
        assertEquals(3, treeTableView.getRow(child3));
    }
    
//    @Test public void removingLastTest() {
//        TreeTableView tree_view = new TreeTableView();
//        MultipleSelectionModel sm = tree_view.getSelectionModel();
//        TreeItem<String> tree_model = new TreeItem<String>("Root");
//        TreeItem node = new TreeItem("Data item");
//        tree_model.getChildren().add(node);
//        tree_view.setRoot(tree_model);
//        tree_model.setExpanded(true);
//        // select the 'Data item' in the selection model
//        sm.select(tree_model.getChildren().get(0));
//        // remove the 'Data item' from the root node
//        tree_model.getChildren().remove(sm.getSelectedItem());
//        // assert the there are no selected items any longer
//        assertTrue("items: " + sm.getSelectedItem(), sm.getSelectedItems().isEmpty());
//    }
    
    /*********************************************************************
     * Tests from bug reports                                            *
     ********************************************************************/  
    @Ignore @Test public void test_rt17112() {
        TreeItem<String> root1 = new TreeItem<String>("Root");
        root1.setExpanded(true);
        addChildren(root1, "child");
        for (TreeItem child : root1.getChildren()) {
            addChildren(child, (String)child.getValue());
            child.setExpanded(true);
        }

        final TreeTableView treeTableView1 = new TreeTableView();
        final MultipleSelectionModel sm = treeTableView1.getSelectionModel();
        sm.setSelectionMode(SelectionMode.MULTIPLE);
        treeTableView1.setRoot(root1);
        
        final TreeItem<String> rt17112_child1 = root1.getChildren().get(1);
        final TreeItem<String> rt17112_child1_0 = rt17112_child1.getChildren().get(0);
        final TreeItem<String> rt17112_child2 = root1.getChildren().get(2);
        
        sm.getSelectedItems().addListener(new InvalidationListener() {
            int count = 0;
            @Override public void invalidated(Observable observable) {
                if (count == 0) {
                    assertEquals(rt17112_child1_0, sm.getSelectedItem());
                    assertEquals(1, sm.getSelectedIndices().size());
                    assertEquals(6, sm.getSelectedIndex());
                    assertTrue(treeTableView1.getFocusModel().isFocused(6));
                } else if (count == 1) {
                    assertEquals(rt17112_child1, sm.getSelectedItem());
                    assertFalse(sm.getSelectedItems().contains(rt17112_child2));
                    assertEquals(1, sm.getSelectedIndices().size());
                    assertTrue(treeTableView1.getFocusModel().isFocused(5));
                }
                count++;
            }
        });
        
        // this triggers the first callback above, so that count == 0
        sm.select(rt17112_child1_0);

        // this triggers the second callback above, so that count == 1
        rt17112_child1.setExpanded(false);
    }
    private void addChildren(TreeItem parent, String name) {
        for (int i=0; i<3; i++) {
            TreeItem<String> ti = new TreeItem<String>(name+"-"+i);
            parent.getChildren().add(ti);
        }
    }
    
    @Test public void test_rt17522_focusShouldMoveWhenItemAddedAtFocusIndex_1() {
        installChildren();
        FocusModel fm = treeTableView.getFocusModel();
        fm.focus(1);    // focus on child1
        assertTrue(fm.isFocused(1));
        assertEquals(child1, fm.getFocusedItem());
        
        TreeItem child0 = new TreeItem("child0");
        root.getChildren().add(0, child0);  // 0th index == position of child1 in root
        
        assertEquals(child1, fm.getFocusedItem());
        assertTrue(fm.isFocused(2));
    }
    
    @Test public void test_rt17522_focusShouldMoveWhenItemAddedBeforeFocusIndex_1() {
        installChildren();
        FocusModel fm = treeTableView.getFocusModel();
        fm.focus(1);    // focus on child1
        assertTrue(fm.isFocused(1));
        
        TreeItem child0 = new TreeItem("child0");
        root.getChildren().add(0, child0);
        assertTrue("Focused index: " + fm.getFocusedIndex(), fm.isFocused(2));
    }
    
    @Test public void test_rt17522_focusShouldNotMoveWhenItemAddedAfterFocusIndex_1() {
        installChildren();
        FocusModel fm = treeTableView.getFocusModel();
        fm.focus(1);    // focus on child1
        assertTrue(fm.isFocused(1));
        
        TreeItem child4 = new TreeItem("child4");
        root.getChildren().add(3, child4);
        assertTrue("Focused index: " + fm.getFocusedIndex(), fm.isFocused(1));
    }
    
    @Test public void test_rt17522_focusShouldBeResetWhenFocusedItemIsRemoved_1() {
        installChildren();
        FocusModel fm = treeTableView.getFocusModel();
        fm.focus(1);
        assertTrue(fm.isFocused(1));
        
        root.getChildren().remove(child1);
        assertEquals(-1, fm.getFocusedIndex());
        assertNull(fm.getFocusedItem());
    }
    
    @Test public void test_rt17522_focusShouldMoveWhenItemRemovedBeforeFocusIndex_1() {
        installChildren();
        FocusModel fm = treeTableView.getFocusModel();
        fm.focus(2);
        assertTrue(fm.isFocused(2));
        
        root.getChildren().remove(child1);
        assertTrue(fm.isFocused(1));
        assertEquals(child2, fm.getFocusedItem());
    }

//    This test fails as, in TreeTableView FocusModel, we do not know the index of the
//    removed tree items, which means we don't know whether they existed before
//    or after the focused item.
//    @Test public void test_rt17522_focusShouldNotMoveWhenItemRemovedAfterFocusIndex() {
//        installChildren();
//        FocusModel fm = treeTableView.getFocusModel();
//        fm.focus(1);
//        assertTrue(fm.isFocused(1));
//        
//        root.getChildren().remove(child3);
//        assertTrue("Focused index: " + fm.getFocusedIndex(), fm.isFocused(1));
//        assertEquals(child1, fm.getFocusedItem());
//    }
    
    @Test public void test_rt18385() {
        installChildren();
//        table.getItems().addAll("row1", "row2", "row3");
        treeTableView.getSelectionModel().select(1);
        treeTableView.getRoot().getChildren().add(new TreeItem("Another Row"));
        assertEquals(1, treeTableView.getSelectionModel().getSelectedIndices().size());
        assertEquals(1, treeTableView.getSelectionModel().getSelectedItems().size());
    }
    
    @Test public void test_rt18339_onlyEditWhenTreeTableViewIsEditable_editableIsFalse() {
        TreeItem root = new TreeItem("root");
        root.getChildren().setAll(
                new TreeItem(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem(new Person("Michael", "Brown", "michael.brown@example.com")));
        root.setExpanded(true);

        TreeTableView<Person> table = new TreeTableView<Person>(root);

        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        table.setEditable(false);
        table.edit(0,firstNameCol);
        assertNull(table.getEditingCell());
    }
    
    @Test public void test_rt18339_onlyEditWhenTreeTableViewIsEditable_editableIsTrue() {
        TreeItem root = new TreeItem("root");
        root.getChildren().setAll(
                new TreeItem(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem(new Person("Michael", "Brown", "michael.brown@example.com")));
        root.setExpanded(true);

        TreeTableView<Person> table = new TreeTableView<Person>(root);

        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        table.setEditable(true);
        table.edit(0,firstNameCol);
        assertEquals(root, table.getEditingCell().getTreeItem());
    }
    
    @Test public void test_rt14451() {
        installChildren();
        treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeTableView.getSelectionModel().selectRange(0, 2); // select from 0 (inclusive) to 2 (exclusive)
        assertEquals(2, treeTableView.getSelectionModel().getSelectedIndices().size());
    }
    
    @Test public void test_rt21586() {
        installChildren();
        treeTableView.getSelectionModel().select(1);
        assertEquals(1, treeTableView.getSelectionModel().getSelectedIndex());
        assertEquals(child1, treeTableView.getSelectionModel().getSelectedItem());
        
        TreeItem root = new TreeItem<String>("New Root");
        TreeItem child1 = new TreeItem<String>("New Child 1");
        TreeItem child2 = new TreeItem<String>("New Child 2");
        TreeItem child3 = new TreeItem<String>("New Child 3");
        root.setExpanded(true);
        root.getChildren().setAll(child1, child2, child3);
        treeTableView.setRoot(root);
        assertEquals(-1, treeTableView.getSelectionModel().getSelectedIndex());
        assertNull(treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Test public void test_rt27181() {
        myCompanyRootNode.setExpanded(true);
        treeTableView.setRoot(myCompanyRootNode);
        
        // start test
        salesDepartment.setExpanded(true);
        treeTableView.getSelectionModel().select(salesDepartment);
        
        assertEquals(1, treeTableView.getFocusModel().getFocusedIndex());
        itSupport.setExpanded(true);
        assertEquals(1, treeTableView.getFocusModel().getFocusedIndex());
    }
    
    @Test public void test_rt27185() {
        myCompanyRootNode.setExpanded(true);
        treeTableView.setRoot(myCompanyRootNode);
        
        // start test
        itSupport.setExpanded(true);
        treeTableView.getSelectionModel().select(mikeGraham);
        
        assertEquals(mikeGraham, treeTableView.getFocusModel().getFocusedItem());
        salesDepartment.setExpanded(true);
        assertEquals(mikeGraham, treeTableView.getFocusModel().getFocusedItem());
    }
    
    @Ignore("Bug hasn't been fixed yet")
    @Test public void test_rt28114() {
        myCompanyRootNode.setExpanded(true);
        treeTableView.setRoot(myCompanyRootNode);
        
        // start test
        itSupport.setExpanded(true);
        treeTableView.getSelectionModel().select(itSupport);
        assertEquals(itSupport, treeTableView.getFocusModel().getFocusedItem());
        assertEquals(itSupport, treeTableView.getSelectionModel().getSelectedItem());
        assertTrue(! itSupport.isLeaf());
        assertTrue(itSupport.isExpanded());
        
        itSupport.getChildren().remove(mikeGraham);
        assertEquals(itSupport, treeTableView.getFocusModel().getFocusedItem());
        assertEquals(itSupport, treeTableView.getSelectionModel().getSelectedItem());
        assertTrue(itSupport.isLeaf());
        assertTrue(!itSupport.isExpanded());
    }
    
    @Test public void test_rt27820_1() {
        TreeItem root = new TreeItem("root");
        root.setExpanded(true);
        TreeItem child = new TreeItem("child");
        root.getChildren().add(child);
        treeTableView.setRoot(root);
        
        treeTableView.getSelectionModel().select(0);
        assertEquals(1, treeTableView.getSelectionModel().getSelectedItems().size());
        assertEquals(root, treeTableView.getSelectionModel().getSelectedItem());
        
        treeTableView.setRoot(null);
        assertEquals(0, treeTableView.getSelectionModel().getSelectedItems().size());
        assertNull(treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Test public void test_rt27820_2() {
        TreeItem root = new TreeItem("root");
        root.setExpanded(true);
        TreeItem child = new TreeItem("child");
        root.getChildren().add(child);
        treeTableView.setRoot(root);
        
        treeTableView.getSelectionModel().select(1);
        assertEquals(1, treeTableView.getSelectionModel().getSelectedItems().size());
        assertEquals(child, treeTableView.getSelectionModel().getSelectedItem());
        
        treeTableView.setRoot(null);
        assertEquals(0, treeTableView.getSelectionModel().getSelectedItems().size());
        assertNull(treeTableView.getSelectionModel().getSelectedItem());
    }
    
    @Test public void test_rt28390() {
        // There should be no NPE when a TreeTableView is shown and the disclosure
        // node is null in a TreeCell
        TreeItem root = new TreeItem("root");
        treeTableView.setRoot(root);
        
        // install a custom cell factory that forces the disclosure node to be
        // null (because by default a null disclosure node will be replaced by
        // a non-null one).
        treeTableView.setRowFactory(new Callback() {
            @Override public Object call(Object p) {
                TreeTableRow treeCell = new TreeTableRow() {
                    {
                        disclosureNodeProperty().addListener((ov, t, t1) -> {
                            setDisclosureNode(null);
                        });
                    }
                    
                    @Override protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item == null ? "" : item.toString());
                    }
                };
                treeCell.setDisclosureNode(null);
                return treeCell;
            }
        });
        
        try {
            Group group = new Group();
            group.getChildren().setAll(treeTableView);
            Scene scene = new Scene(group);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (NullPointerException e) {
            System.out.println("A null disclosure node is valid, so we shouldn't have an NPE here.");
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    @Ignore("This test begun failing when createDefaultCellImpl was removed from TreeTableViewSkin on 28/3/2013")
    @Test public void test_rt28534() {
        TreeItem root = new TreeItem("root");
        root.getChildren().setAll(
                new TreeItem(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem(new Person("Michael", "Brown", "michael.brown@example.com")));
        root.setExpanded(true);
        
        TreeTableView<Person> table = new TreeTableView<Person>(root);
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("lastName"));

        TreeTableColumn emailCol = new TreeTableColumn("Email");
        emailCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("email"));

        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

        VirtualFlowTestUtils.assertRowsNotEmpty(table, 0, 6); // rows 0 - 6 should be filled
        VirtualFlowTestUtils.assertRowsEmpty(table, 6, -1); // rows 6+ should be empty
        
        // now we replace the data and expect the cells that have no data
        // to be empty
        root.getChildren().setAll(
                new TreeItem(new Person("*_*Emma", "Jones", "emma.jones@example.com")),
                new TreeItem(new Person("_Michael", "Brown", "michael.brown@example.com")));
        
        VirtualFlowTestUtils.assertRowsNotEmpty(table, 0, 3); // rows 0 - 3 should be filled
        VirtualFlowTestUtils.assertRowsEmpty(table, 3, -1); // rows 3+ should be empty
    }
    
    @Test public void test_rt22463() {
        final TreeTableView<RT_22463_Person> table = new TreeTableView<RT_22463_Person>();
        table.setTableMenuButtonVisible(true);
        TreeTableColumn c1 = new TreeTableColumn("Id");
        TreeTableColumn c2 = new TreeTableColumn("Name");
        c1.setCellValueFactory(new TreeItemPropertyValueFactory<Person, Long>("id"));
        c2.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("name"));
        table.getColumns().addAll(c1, c2);
        
        RT_22463_Person rootPerson = new RT_22463_Person();
        rootPerson.setName("Root");
        TreeItem<RT_22463_Person> root = new TreeItem<RT_22463_Person>(rootPerson);
        root.setExpanded(true);
        
        table.setRoot(root);
        
        // before the change things display fine
        RT_22463_Person p1 = new RT_22463_Person();
        p1.setId(1l);
        p1.setName("name1");
        RT_22463_Person p2 = new RT_22463_Person();
        p2.setId(2l);
        p2.setName("name2");
        root.getChildren().addAll(
                new TreeItem<RT_22463_Person>(p1), 
                new TreeItem<RT_22463_Person>(p2));
        VirtualFlowTestUtils.assertCellTextEquals(table, 1, "1", "name1");
        VirtualFlowTestUtils.assertCellTextEquals(table, 2, "2", "name2");
        
        // now we change the persons but they are still equal as the ID's don't
        // change - but the items list is cleared so the cells should update
        RT_22463_Person new_p1 = new RT_22463_Person();
        new_p1.setId(1l);
        new_p1.setName("updated name1");
        RT_22463_Person new_p2 = new RT_22463_Person();
        new_p2.setId(2l);
        new_p2.setName("updated name2");
        root.getChildren().clear();
        root.getChildren().setAll(
                new TreeItem<RT_22463_Person>(new_p1), 
                new TreeItem<RT_22463_Person>(new_p2));
        VirtualFlowTestUtils.assertCellTextEquals(table, 1, "1", "updated name1");
        VirtualFlowTestUtils.assertCellTextEquals(table, 2, "2", "updated name2");
    }
    
    @Test public void test_rt28637() {
        TreeItem<String> s1, s2, s3, s4;
        ObservableList<TreeItem<String>> items = FXCollections.observableArrayList(
                s1 = new TreeItem<String>("String1"), 
                s2 = new TreeItem<String>("String2"), 
                s3 = new TreeItem<String>("String3"), 
                s4 = new TreeItem<String>("String4"));
        
        final TreeTableView<String> treeTableView = new TreeTableView<String>();
        
        TreeItem<String> root = new TreeItem<String>("Root");
        root.setExpanded(true);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
        root.getChildren().addAll(items);
        
        treeTableView.getSelectionModel().select(0);
        assertEquals((Object)s1, treeTableView.getSelectionModel().getSelectedItem());
        assertEquals((Object)s1, treeTableView.getSelectionModel().getSelectedItems().get(0));
        assertEquals(0, treeTableView.getSelectionModel().getSelectedIndex());
        
        root.getChildren().remove(treeTableView.getSelectionModel().getSelectedItem());
        assertEquals((Object)s2, treeTableView.getSelectionModel().getSelectedItem());
        assertEquals((Object)s2, treeTableView.getSelectionModel().getSelectedItems().get(0));
        assertEquals(0, treeTableView.getSelectionModel().getSelectedIndex());
    }
    
    @Test public void test_rt24844() {
        // p1 == lowest first name
        TreeItem<Person> p0, p1, p2, p3, p4;
        
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
            p3 = new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
            p2 = new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
            p1 = new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
            p0 = new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
            p4 = new TreeItem<Person>(new Person("Michael", "Brown", "michael.brown@example.com")));
            
        TreeTableView<Person> table = new TreeTableView<>();
        
        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));
        
        // set dummy comparator to lock items in place until new comparator is set
        firstNameCol.setComparator((t, t1) -> 0);

        table.getColumns().addAll(firstNameCol);
        table.getSortOrder().add(firstNameCol);
        
        // ensure the existing order is as expected
        assertEquals(p3, root.getChildren().get(0));
        assertEquals(p2, root.getChildren().get(1));
        assertEquals(p1, root.getChildren().get(2));
        assertEquals(p0, root.getChildren().get(3));
        assertEquals(p4, root.getChildren().get(4));
        
        // set a new comparator
        firstNameCol.setComparator(new Comparator() {
            Random r =  new Random();
            @Override public int compare(Object t, Object t1) {
                return t.toString().compareTo(t1.toString());
            }
        });
        
        // ensure the new order is as expected
        assertEquals(p0, root.getChildren().get(0));
        assertEquals(p1, root.getChildren().get(1));
        assertEquals(p2, root.getChildren().get(2));
        assertEquals(p3, root.getChildren().get(3));
        assertEquals(p4, root.getChildren().get(4));
    }
    
    @Test public void test_rt29331() {
        TreeTableView<Person> table = new TreeTableView<Person>();
        
        // p1 == lowest first name
        TreeItem<Person> p0, p1, p2, p3, p4;
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));

        TreeTableColumn emailCol = new TreeTableColumn("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        
        TreeTableColumn parentColumn = new TreeTableColumn<>("Parent");
        parentColumn.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        
        table.getColumns().addAll(parentColumn);
        
        // table is setup, now hide the 'last name' column
        emailCol.setVisible(false);
        assertFalse(emailCol.isVisible());
        
        // reorder columns inside the parent column
        parentColumn.getColumns().setAll(emailCol, firstNameCol, lastNameCol);
        
        // the email column should not become visible after this, but it does
        assertFalse(emailCol.isVisible());
    }
    
    private int rt29330_count = 0;
    @Test public void test_rt29330_1() {
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Michael", "Brown", "michael.brown@example.com")));
                
        TreeTableView<Person> table = new TreeTableView<>();
        
        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);
        
        TreeTableColumn parentColumn = new TreeTableColumn<>("Parent");
        table.getColumns().addAll(parentColumn);
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("lastName"));
        
        parentColumn.getColumns().addAll(firstNameCol, lastNameCol);

        table.setOnSort(event -> {
            rt29330_count++;
        });
        
        // test preconditions
        assertEquals(ASCENDING, lastNameCol.getSortType());
        assertEquals(0, rt29330_count);
        
        table.getSortOrder().add(lastNameCol);
        assertEquals(1, rt29330_count);
        
        lastNameCol.setSortType(DESCENDING);
        assertEquals(2, rt29330_count);
        
        lastNameCol.setSortType(null);
        assertEquals(3, rt29330_count);
        
        lastNameCol.setSortType(ASCENDING);
        assertEquals(4, rt29330_count);
    }
    
    @Test public void test_rt29330_2() {
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Michael", "Brown", "michael.brown@example.com")));
                
        TreeTableView<Person> table = new TreeTableView<>();
        
        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("lastName"));
        
        // this test differs from the previous one by installing the parent column
        // into the tableview after it has the children added into it
        TreeTableColumn parentColumn = new TreeTableColumn<>("Parent");
        parentColumn.getColumns().addAll(firstNameCol, lastNameCol);
        table.getColumns().addAll(parentColumn);

        table.setOnSort(event -> {
            rt29330_count++;
        });
        
        // test preconditions
        assertEquals(ASCENDING, lastNameCol.getSortType());
        assertEquals(0, rt29330_count);
        
        table.getSortOrder().add(lastNameCol);
        assertEquals(1, rt29330_count);
        
        lastNameCol.setSortType(DESCENDING);
        assertEquals(2, rt29330_count);
        
        lastNameCol.setSortType(null);
        assertEquals(3, rt29330_count);
        
        lastNameCol.setSortType(ASCENDING);
        assertEquals(4, rt29330_count);
    }
    
    @Test public void test_rt29313_selectedIndices() {
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Michael", "Brown", "michael.brown@example.com")));
                
        TreeTableView<Person> table = new TreeTableView<>();
        
        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);
        
        TableSelectionModel sm = table.getSelectionModel();
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("lastName"));
        
        TreeTableColumn emailCol = new TreeTableColumn("Email");
        emailCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("email"));
        
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        sm.setCellSelectionEnabled(true);
        sm.setSelectionMode(SelectionMode.MULTIPLE);
        
        assertTrue(sm.getSelectedIndices().isEmpty());
        
        // only (0,0) should be selected, so selected indices should be [0]
        sm.select(0, firstNameCol);
        assertEquals(1, sm.getSelectedIndices().size());
        
        // now (0,0) and (1,0) should be selected, so selected indices should be [0, 1]
        sm.select(1, firstNameCol);
        assertEquals(2, sm.getSelectedIndices().size());
        
        // now (0,0), (1,0) and (1,1) should be selected, but selected indices 
        // should remain as [0, 1], as we don't want selected indices to become
        // [0,1,1] (which is what RT-29313 is about)
        sm.select(1, lastNameCol);
        assertEquals(2, sm.getSelectedIndices().size());
        assertEquals(0, sm.getSelectedIndices().get(0));
        assertEquals(1, sm.getSelectedIndices().get(1));
    }
    
    @Test public void test_rt29313_selectedItems() {
        TreeItem<Person> p0, p1;
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
                p0 = new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                p1 = new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Michael", "Brown", "michael.brown@example.com")));
                
        TreeTableView<Person> table = new TreeTableView<>();
        
        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);
        
        TableSelectionModel sm = table.getSelectionModel();
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("lastName"));
        
        TreeTableColumn emailCol = new TreeTableColumn("Email");
        emailCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("email"));
        
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        sm.setCellSelectionEnabled(true);
        sm.setSelectionMode(SelectionMode.MULTIPLE);
        
        assertTrue(sm.getSelectedItems().isEmpty());
        
        // only (0,0) should be selected, so selected items should be [p0]
        sm.select(0, firstNameCol);
        assertEquals(1, sm.getSelectedItems().size());
        
        // now (0,0) and (1,0) should be selected, so selected items should be [p0, p1]
        sm.select(1, firstNameCol);
        assertEquals(2, sm.getSelectedItems().size());
        
        // now (0,0), (1,0) and (1,1) should be selected, but selected items 
        // should remain as [p0, p1], as we don't want selected items to become
        // [p0,p1,p1] (which is what RT-29313 is about)
        sm.select(1, lastNameCol);
        assertEquals(2, sm.getSelectedItems().size());
        assertEquals(p0, sm.getSelectedItems().get(0));
        assertEquals(p1, sm.getSelectedItems().get(1));
    }
    
    @Test public void test_rt29566() {
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Michael", "Brown", "michael.brown@example.com")));
                
        TreeTableView<Person> table = new TreeTableView<>();
        
        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);
        
        TableSelectionModel sm = table.getSelectionModel();
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn lastNameCol = new TreeTableColumn("Last Name");
        lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("lastName"));
        
        TreeTableColumn emailCol = new TreeTableColumn("Email");
        emailCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("email"));
        
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        
        // test the state before we hide and re-add a column
        VirtualFlowTestUtils.assertCellTextEquals(table, 0, "Jacob", "Smith", "jacob.smith@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 1, "Isabella", "Johnson", "isabella.johnson@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 2, "Ethan", "Williams", "ethan.williams@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 3, "Emma", "Jones", "emma.jones@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 4, "Michael", "Brown", "michael.brown@example.com");
        
        // hide the last name column, and test cells again
        table.getColumns().remove(lastNameCol);
        VirtualFlowTestUtils.assertCellTextEquals(table, 0, "Jacob", "jacob.smith@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 1, "Isabella", "isabella.johnson@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 2, "Ethan", "ethan.williams@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 3, "Emma", "emma.jones@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 4, "Michael", "michael.brown@example.com");
        
        // re-add the last name column - we should go back to the original state.
        // However, what appears to be happening is that, for some reason, some
        // of the cells from the removed column do not reappear - meaning in this case
        // some of the last name values will not be where we expect them to be.
        // This is clearly not ideal!
        table.getColumns().add(1, lastNameCol);
        VirtualFlowTestUtils.assertCellTextEquals(table, 0, "Jacob", "Smith", "jacob.smith@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 1, "Isabella", "Johnson", "isabella.johnson@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 2, "Ethan", "Williams", "ethan.williams@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 3, "Emma", "Jones", "emma.jones@example.com");
        VirtualFlowTestUtils.assertCellTextEquals(table, 4, "Michael", "Brown", "michael.brown@example.com");
    }
    
    @Test public void test_rt29390() {
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")),
                new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com")),
                new TreeItem<Person>(new Person("Isabella", "Johnson", "isabella.johnson@example.com")),
                new TreeItem<Person>(new Person("Ethan", "Williams", "ethan.williams@example.com")),
                new TreeItem<Person>(new Person("Emma", "Jones", "emma.jones@example.com")
        ));
                
        TreeTableView<Person> table = new TreeTableView<>();
        table.setMaxHeight(50);
        table.setPrefHeight(50);
        
        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);
        
        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));
        
        table.getColumns().add(firstNameCol);
        
        Toolkit.getToolkit().firePulse();
        
        // we want the vertical scrollbar
        VirtualScrollBar scrollBar = VirtualFlowTestUtils.getVirtualFlowVerticalScrollbar(table);
        
        assertNotNull(scrollBar);
        assertTrue(scrollBar.isVisible());
        assertTrue(scrollBar.getVisibleAmount() > 0.0);
        assertTrue(scrollBar.getVisibleAmount() < 1.0);
        
        // this next test is likely to be brittle, but we'll see...If it is the
        // cause of failure then it can be commented out
        assertEquals(0.0625, scrollBar.getVisibleAmount(), 0.0);
    }
    
    @Test public void test_rt29676_withText() {
        // set up test
        TreeTableView<Data> treeTableView = new TreeTableView<Data>();
        treeTableView.setMaxWidth(100);
        
        TreeItem<Data> root = new TreeItem<Data>(new Data("Root"));
        treeTableView.setRoot(root);
        addLevel(root, 0, 30);

        treeTableView.getRoot().setExpanded(true);
        TreeTableColumn<Data, String> column = new TreeTableColumn<Data, String>("Items' name");
        column.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getValue().getData()));
        treeTableView.getColumns().add(column);

        // show treeTableView
        StageLoader sl = new StageLoader(treeTableView);

        // expand all collapsed branches
        root.setExpanded(true);
        for (int i = 0; i < root.getChildren().size(); i++) {
            TreeItem<Data> child = root.getChildren().get(i);
            child.setExpanded(true);
        }
        
        // get all cells and ensure their content is as expected
        int cellCount = VirtualFlowTestUtils.getCellCount(treeTableView);
        for (int i = 0; i < cellCount; i++) {
            // get the TreeTableRow
            final TreeTableRow rowCell = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, i);
            final TreeItem treeItem = rowCell.getTreeItem();
            if (treeItem == null) continue;
            
            final boolean isBranch = ! treeItem.isLeaf();
            
            // then check its children
            List<Node> children = rowCell.getChildrenUnmodifiable();
            for (int j = 0; j < children.size(); j++) {
                final Node child = children.get(j);
                
                assertTrue(child.isVisible());
                assertNotNull(child.getParent());
                assertNotNull(child.getScene());
                
                if (child.getStyleClass().contains("tree-disclosure-node")) {
                    // no-op
                } 
                
                if (child.getStyleClass().contains("tree-table-cell")) {
                    TreeTableCell cell = (TreeTableCell) child;
                    assertNotNull(cell.getText());
                    assertFalse(cell.getText().isEmpty());
                }
            }
        }

        sl.dispose();
    }
    private void addLevel(TreeItem<Data> item, int level, int length) {
        for (int i = 0; i < 3; i++) {
            StringBuilder builder = new StringBuilder();
            builder.append("Level " + level + " Item " + item);
            if (length > 0) {
                builder.append(" l");
                for (int j = 0; j < length; j++) {
                    builder.append("o");
                }
                builder.append("ng");
            }
            String itemString = builder.toString();
            TreeItem<Data> child = new TreeItem<Data>(new Data(itemString));
            if (level < 3 - 1) {
                addLevel(child, level + 1, length);
            }
            item.getChildren().add(child);
        }
    }
    
    @Test public void test_rt27180_collapseBranch_childSelected_singleSelection() {
        sm.setCellSelectionEnabled(false);
        sm.setSelectionMode(SelectionMode.SINGLE);
        
        treeTableView.setRoot(myCompanyRootNode);
        myCompanyRootNode.setExpanded(true);
        salesDepartment.setExpanded(true);
        itSupport.setExpanded(true);
        sm.select(2);                   // ethanWilliams
        assertFalse(sm.isSelected(1));  // salesDepartment
        assertTrue(sm.isSelected(2));   // ethanWilliams
        assertTrue(treeTableView.getFocusModel().isFocused(2));
        assertEquals(1, sm.getSelectedCells().size());
        
        // now collapse the salesDepartment, selection should
        // not jump down to the itSupport people
        salesDepartment.setExpanded(false);
        assertTrue(sm.getSelectedIndices().toString(), sm.isSelected(1));   // salesDepartment
        assertTrue(treeTableView.getFocusModel().isFocused(1));
        assertEquals(1, sm.getSelectedCells().size());
    }
    
    @Test public void test_rt27180_collapseBranch_laterSiblingSelected_singleSelection() {
        sm.setCellSelectionEnabled(false);
        sm.setSelectionMode(SelectionMode.SINGLE);
        
        treeTableView.setRoot(myCompanyRootNode);
        myCompanyRootNode.setExpanded(true);
        salesDepartment.setExpanded(true);
        itSupport.setExpanded(true);
        sm.select(8);                   // itSupport
        assertFalse(sm.isSelected(1));  // salesDepartment
        assertTrue(sm.isSelected(8));   // itSupport
        assertTrue(treeTableView.getFocusModel().isFocused(8));
        assertEquals(1, sm.getSelectedIndices().size());
        
        salesDepartment.setExpanded(false);
        assertTrue(debug(), sm.isSelected(2));   // itSupport
        assertTrue(treeTableView.getFocusModel().isFocused(2));
        assertEquals(1, sm.getSelectedIndices().size());
    }
    
    @Test public void test_rt27180_collapseBranch_laterSiblingAndChildrenSelected() {
        sm.setSelectionMode(SelectionMode.MULTIPLE);
        sm.setCellSelectionEnabled(false);
        
        treeTableView.setRoot(myCompanyRootNode);
        myCompanyRootNode.setExpanded(true);
        salesDepartment.setExpanded(true);
        itSupport.setExpanded(true);
        sm.selectIndices(8, 9, 10);     // itSupport, and two people
        assertFalse(sm.isSelected(1));  // salesDepartment
        assertTrue(sm.isSelected(8));   // itSupport
        assertTrue(sm.isSelected(9));   // mikeGraham
        assertTrue(sm.isSelected(10));  // judyMayer
        assertTrue(treeTableView.getFocusModel().isFocused(10));
        assertEquals(3, sm.getSelectedIndices().size());
        
        salesDepartment.setExpanded(false);
        assertTrue(debug(), sm.isSelected(2));   // itSupport
        assertTrue(sm.isSelected(3));   // mikeGraham
        assertTrue(sm.isSelected(4));   // judyMayer
        assertTrue(treeTableView.getFocusModel().isFocused(4));
        assertEquals(3, sm.getSelectedIndices().size());
    }
    
    @Test public void test_rt27180_expandBranch_laterSiblingSelected_singleSelection() {
        sm.setCellSelectionEnabled(false);
        sm.setSelectionMode(SelectionMode.SINGLE);
        
        treeTableView.setRoot(myCompanyRootNode);
        myCompanyRootNode.setExpanded(true);
        salesDepartment.setExpanded(false);
        itSupport.setExpanded(true);
        sm.select(2);                   // itSupport
        assertFalse(sm.isSelected(1));  // salesDepartment
        assertTrue(sm.isSelected(2));   // itSupport
        assertTrue(treeTableView.getFocusModel().isFocused(2));
        assertEquals(1, sm.getSelectedIndices().size());
        
        salesDepartment.setExpanded(true);
        assertTrue(debug(), sm.isSelected(8));   // itSupport
        assertTrue(treeTableView.getFocusModel().isFocused(8));
        assertEquals(1, sm.getSelectedIndices().size());
    }
    
    @Test public void test_rt27180_expandBranch_laterSiblingAndChildrenSelected() {
        sm.setSelectionMode(SelectionMode.MULTIPLE);
        sm.setCellSelectionEnabled(false);
        
        treeTableView.setRoot(myCompanyRootNode);
        myCompanyRootNode.setExpanded(true);
        salesDepartment.setExpanded(false);
        itSupport.setExpanded(true);
        sm.selectIndices(2,3,4);     // itSupport, and two people
        assertFalse(sm.isSelected(1));  // salesDepartment
        assertTrue(sm.isSelected(2));   // itSupport
        assertTrue(sm.isSelected(3));   // mikeGraham
        assertTrue(sm.isSelected(4));  // judyMayer
        assertTrue(treeTableView.getFocusModel().isFocused(4));
        assertEquals(3, sm.getSelectedIndices().size());
        
        salesDepartment.setExpanded(true);
        assertTrue(debug(), sm.isSelected(8));   // itSupport
        assertTrue(sm.isSelected(9));   // mikeGraham
        assertTrue(sm.isSelected(10));   // judyMayer
        assertTrue(treeTableView.getFocusModel().isFocused(10));
        assertEquals(3, sm.getSelectedIndices().size());
    }

    @Test public void test_rt30400() {
        // create a treetableview that'll render cells using the check box cell factory
        TreeItem<String> rootItem = new TreeItem<>("root");
        final TreeTableView<String> tableView = new TreeTableView<String>(rootItem);
        tableView.setMinHeight(100);
        tableView.setPrefHeight(100);

        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));
        firstNameCol.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(param -> new ReadOnlyBooleanWrapper(true)));
        tableView.getColumns().add(firstNameCol);

        // because only the first row has data, all other rows should be
        // empty (and not contain check boxes - we just check the first four here)
        VirtualFlowTestUtils.assertRowsNotEmpty(tableView, 0, 1);
        VirtualFlowTestUtils.assertCellNotEmpty(VirtualFlowTestUtils.getCell(tableView, 0));
        VirtualFlowTestUtils.assertCellEmpty(VirtualFlowTestUtils.getCell(tableView, 1));
        VirtualFlowTestUtils.assertCellEmpty(VirtualFlowTestUtils.getCell(tableView, 2));
        VirtualFlowTestUtils.assertCellEmpty(VirtualFlowTestUtils.getCell(tableView, 3));
    }

    @Ignore("This bug is not yet fixed")
    @Test public void test_rt31165() {
        installChildren();
        treeTableView.setEditable(true);

        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper("TEST"));
        firstNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        firstNameCol.setEditable(true);

        treeTableView.getColumns().add(firstNameCol);

        IndexedCell cell = VirtualFlowTestUtils.getCell(treeTableView, 1, 0);
        assertEquals("TEST", cell.getText());
        assertFalse(cell.isEditing());

        treeTableView.edit(1, firstNameCol);

        assertEquals(child1, treeTableView.getEditingCell().getTreeItem());
        assertTrue(cell.isEditing());

        VirtualFlowTestUtils.getVirtualFlow(treeTableView).requestLayout();
        Toolkit.getToolkit().firePulse();

        assertEquals(child1, treeTableView.getEditingCell().getTreeItem());
        assertTrue(cell.isEditing());
    }

    @Test public void test_rt31404() {
        installChildren();

        TreeTableColumn<String,String> firstNameCol = new TreeTableColumn<>("First Name");
        firstNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue()));

        treeTableView.getColumns().add(firstNameCol);

        IndexedCell cell = VirtualFlowTestUtils.getCell(treeTableView, 0, 0);
        assertEquals("Root", cell.getText());

        treeTableView.setShowRoot(false);
        assertEquals("Child 1", cell.getText());
    }

    @Test public void test_rt31471() {
        installChildren();

        TreeTableColumn<String,String> firstNameCol = new TreeTableColumn<>("First Name");
        firstNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue()));

        treeTableView.getColumns().add(firstNameCol);

        IndexedCell cell = VirtualFlowTestUtils.getCell(treeTableView, 0);
        assertEquals("Root", cell.getItem());

        treeTableView.setFixedCellSize(50);

        VirtualFlowTestUtils.getVirtualFlow(treeTableView).requestLayout();
        Toolkit.getToolkit().firePulse();

        assertEquals("Root", cell.getItem());
        assertEquals(50, cell.getHeight(), 0.00);
    }

    @Test public void test_rt30466() {
        final Node graphic1 = new Circle(6.75, Color.RED);
        final Node graphic2 = new Circle(6.75, Color.GREEN);

        installChildren();

        TreeTableColumn<String,String> firstNameCol = new TreeTableColumn<>("First Name");
        firstNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue()));

        treeTableView.getColumns().add(firstNameCol);

        TreeTableRow cell = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 0);
        assertEquals("Root", cell.getItem());

        // set the first graphic - which we expect to see as a child of the cell
        root.setGraphic(graphic1);
        cell = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 0);
        boolean matchGraphic1 = false;
        boolean matchGraphic2 = false;
        for (Node n : cell.getChildrenUnmodifiable()) {
            if (n == graphic1) {
                matchGraphic1 = true;
            }
            if (n == graphic2) {
                matchGraphic2 = true;
            }
        }
        assertTrue(matchGraphic1);
        assertFalse(matchGraphic2);

        // set the second graphic - which we also expect to see - but of course graphic1 should not be a child any longer
        root.setGraphic(graphic2);
        cell = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 0);
        matchGraphic1 = false;
        matchGraphic2 = false;
        for (Node n : cell.getChildrenUnmodifiable()) {
            if (n == graphic1) {
                matchGraphic1 = true;
            }
            if (n == graphic2) {
                matchGraphic2 = true;
            }
        }
        assertFalse(matchGraphic1);
        assertTrue(matchGraphic2);
    }

    private int rt_31200_count = 0;
    @Test public void test_rt_31200_tableCell() {
        rt_31200_count = 0;

        installChildren();
        TreeTableColumn<String,String> firstNameCol = new TreeTableColumn<>("First Name");
        firstNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue()));
        treeTableView.getColumns().add(firstNameCol);

        firstNameCol.setCellFactory(new Callback<TreeTableColumn<String, String>, TreeTableCell<String, String>>() {
            @Override
            public TreeTableCell<String, String> call(TreeTableColumn<String, String> param) {
                return new TreeTableCell<String, String>() {
                    ImageView view = new ImageView();

                    {
                        setGraphic(view);
                    }

                    ;

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (getItem() == null ? item == null : getItem().equals(item)) {
                            rt_31200_count++;
                        }
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            view.setImage(null);
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                };
            }
        });

        StageLoader sl = new StageLoader(treeTableView);

        assertEquals(12, rt_31200_count);

        // resize the stage
        sl.getStage().setHeight(250);
        Toolkit.getToolkit().firePulse();
        sl.getStage().setHeight(50);
        Toolkit.getToolkit().firePulse();
        assertEquals(12, rt_31200_count);

        sl.dispose();
    }

    @Test public void test_rt_31200_tableRow() {
        rt_31200_count = 0;

        installChildren();
        TreeTableColumn<String,String> firstNameCol = new TreeTableColumn<>("First Name");
        firstNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue()));
        treeTableView.getColumns().add(firstNameCol);

        treeTableView.setRowFactory(new Callback<TreeTableView<String>, TreeTableRow<String>>() {
            @Override
            public TreeTableRow<String> call(TreeTableView<String> param) {
                return new TreeTableRow<String>() {
                    ImageView view = new ImageView();

                    {
                        setGraphic(view);
                    }

                    ;

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (getItem() == null ? item == null : getItem().equals(item)) {
                            rt_31200_count++;
                        }
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            view.setImage(null);
                            setText(null);
                        } else {
                            setText(item.toString());
                        }
                    }
                };
            }
        });

        StageLoader sl = new StageLoader(treeTableView);

        assertEquals(17, rt_31200_count);

        // resize the stage
        sl.getStage().setHeight(250);
        Toolkit.getToolkit().firePulse();
        sl.getStage().setHeight(50);
        Toolkit.getToolkit().firePulse();
        assertEquals(17, rt_31200_count);

        sl.dispose();
    }

    @Test public void test_rt_31727() {
        installChildren();
        treeTableView.setEditable(true);

        TreeTableColumn firstNameCol = new TreeTableColumn("First Name");
        firstNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper("TEST"));
        firstNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        firstNameCol.setEditable(true);

        treeTableView.getColumns().add(firstNameCol);

        treeTableView.setEditable(true);
        firstNameCol.setEditable(true);

        // do a normal edit
        treeTableView.edit(0, firstNameCol);
        TreeTablePosition editingCell = treeTableView.getEditingCell();
        assertNotNull(editingCell);
        assertEquals(0, editingCell.getRow());
        assertEquals(0, editingCell.getColumn());
        assertEquals(firstNameCol, editingCell.getTableColumn());
        assertEquals(treeTableView, editingCell.getTreeTableView());

        // cancel editing
        treeTableView.edit(-1, null);
        editingCell = treeTableView.getEditingCell();
        assertNull(editingCell);
    }

    @Test public void test_rt_21517() {
        installChildren();

//        final TableSelectionModel sm = t.getSelectionModel();
        TreeTableColumn<String, String> col = new TreeTableColumn<String, String>("column");
        col.setSortType(ASCENDING);
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        // test pre-conditions
        assertTrue(sm.isEmpty());

        // select the 4th row (that is, the third child of the root)
        sm.select(3);
        assertTrue(sm.isSelected(3));
        assertEquals(3, sm.getSelectedIndex());
        assertEquals(1, sm.getSelectedIndices().size());
        assertTrue(sm.getSelectedIndices().contains(3));
        assertEquals(child3, sm.getSelectedItem());
        assertEquals(1, sm.getSelectedItems().size());
        assertTrue(sm.getSelectedItems().contains(child3));

        // we also want to test visually
        TreeTableRow rootRow = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 0);
        assertFalse(rootRow.isSelected());
        TreeTableRow child3Row = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 3);
        assertTrue(child3Row.isSelected());

        // sort tableview by firstname column in ascending (default) order
        // (so aaa continues to come first)
        treeTableView.getSortOrder().add(col);

        // nothing should have changed
        assertTrue(sm.isSelected(3));
        assertEquals(3, sm.getSelectedIndex());
        assertEquals(1, sm.getSelectedIndices().size());
        assertTrue(sm.getSelectedIndices().contains(3));
        assertEquals(child3, sm.getSelectedItem());
        assertEquals(1, sm.getSelectedItems().size());
        assertTrue(sm.getSelectedItems().contains(child3));
        rootRow = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 0);
        assertFalse(rootRow.isSelected());
        child3Row = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 3);
        assertTrue(child3Row.isSelected());

        // continue to sort tableview by firstname column, but now in descending
        // order, (so ccc to come first)
        col.setSortType(TreeTableColumn.SortType.DESCENDING);

        // now test to ensure that CCC is still the only selected item, but now
        // located in index 1 (as the first child of the root)
        assertTrue(debug(), sm.isSelected(1));
        assertEquals(1, sm.getSelectedIndex());
        assertEquals(1, sm.getSelectedIndices().size());
        assertTrue(sm.getSelectedIndices().contains(1));
        assertEquals(child3, sm.getSelectedItem());
        assertEquals(1, sm.getSelectedItems().size());
        assertTrue(sm.getSelectedItems().contains(child3));

        // we also want to test visually
        rootRow = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 0);
        assertFalse(rootRow.isSelected());
        child3Row = (TreeTableRow) VirtualFlowTestUtils.getCell(treeTableView, 1);
        assertTrue(child3Row.isSelected());
    }

    @Test public void test_rt_30484_treeTableCell() {
        installChildren();

        TreeTableColumn<String, String> col = new TreeTableColumn<String, String>("column");
        col.setSortType(ASCENDING);
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        col.setCellFactory(new Callback<TreeTableColumn<String, String>, TreeTableCell<String, String>>() {
            @Override
            public TreeTableCell<String, String> call(TreeTableColumn<String, String> param) {
                return new TreeTableCell<String, String>() {
                    Rectangle graphic = new Rectangle(10, 10, Color.RED);
                    { setGraphic(graphic); };

                    @Override protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            graphic.setVisible(false);
                            setText(null);
                        } else {
                            graphic.setVisible(true);
                            setText(item);
                        }
                    }
                };
            }
        });

        // First four rows have content, so the graphic should show.
        // All other rows have no content, so graphic should not show.

        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    0, 0);
        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    1, 0);
        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    2, 0);
        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    3, 0);
        VirtualFlowTestUtils.assertGraphicIsNotVisible(treeTableView, 4, 0);
        VirtualFlowTestUtils.assertGraphicIsNotVisible(treeTableView, 5, 0);
    }

    @Test public void test_rt_30484_treeTableRow() {
        installChildren();

        TreeTableColumn<String, String> col = new TreeTableColumn<String, String>("column");
        col.setSortType(ASCENDING);
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        treeTableView.setRowFactory(new Callback<TreeTableView<String>, TreeTableRow<String>>() {
            @Override public TreeTableRow<String> call(TreeTableView<String> param) {
                return new TreeTableRow<String>() {
                    Rectangle graphic = new Rectangle(10, 10, Color.RED);
                    { setGraphic(graphic); };

                    @Override protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            graphic.setVisible(false);
                            setText(null);
                        } else {
                            graphic.setVisible(true);
                            setText(item.toString());
                        }
                    }
                };
            }
        });

        // First two rows have content, so the graphic should show.
        // All other rows have no content, so graphic should not show.

        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    0);
        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    1);
        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    2);
        VirtualFlowTestUtils.assertGraphicIsVisible(treeTableView,    3);
        VirtualFlowTestUtils.assertGraphicIsNotVisible(treeTableView, 4);
        VirtualFlowTestUtils.assertGraphicIsNotVisible(treeTableView, 5);
    }

    private int rt_31015_count = 0;
    @Test public void test_rt_31015() {
        installChildren();
        root.getChildren().clear();
        treeTableView.setEditable(true);

        TreeTableColumn<String, String> col = new TreeTableColumn<String, String>("column");
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        //Set cell factory for cells that allow editing
        Callback<TreeTableColumn<String,String>, TreeTableCell<String, String>> cellFactory = new Callback<TreeTableColumn<String,String>, TreeTableCell<String, String>>() {
            public TreeTableCell<String, String> call(TreeTableColumn<String, String> p) {
                return new TreeTableCell<String, String>() {
                    @Override public void cancelEdit() {
                        super.cancelEdit();
                        rt_31015_count++;
                    }
                };
            }
        };
        col.setCellFactory(cellFactory);

        StageLoader sl = new StageLoader(treeTableView);

        assertEquals(0, rt_31015_count);

        treeTableView.edit(0, col);
        assertEquals(0, rt_31015_count);

        treeTableView.edit(-1, null);
        assertEquals(1, rt_31015_count);

        sl.dispose();
    }

    @Test public void test_rt_30688() {
        installChildren();
        root.getChildren().clear();
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        TreeTableColumn<String, String> col = new TreeTableColumn<>("column");
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        StageLoader sl = new StageLoader(treeTableView);

        assertEquals(treeTableView.contentWidth, col.getWidth(), 0.0);

        sl.dispose();
    }

    private int rt_29650_start_count = 0;
    private int rt_29650_commit_count = 0;
    private int rt_29650_cancel_count = 0;
    @Test public void test_rt_29650() {
        installChildren();
        treeTableView.setEditable(true);

        TreeTableColumn<String, String> col = new TreeTableColumn<>("column");
        Callback<TreeTableColumn<String, String>, TreeTableCell<String, String>> factory = TextFieldTreeTableCell.forTreeTableColumn();
        col.setCellFactory(factory);
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        col.setOnEditStart(t -> {
            rt_29650_start_count++;
        });
        col.setOnEditCommit(t -> {
            rt_29650_commit_count++;
        });
        col.setOnEditCancel(t -> {
            rt_29650_cancel_count++;
        });

        StageLoader sl = new StageLoader(treeTableView);

        treeTableView.edit(0, col);

        Toolkit.getToolkit().firePulse();

        TreeTableCell rootCell = (TreeTableCell) VirtualFlowTestUtils.getCell(treeTableView, 0, 0);
        TextField textField = (TextField) rootCell.getGraphic();
        textField.setText("Testing!");
        KeyEventFirer keyboard = new KeyEventFirer(textField);
        keyboard.doKeyPress(KeyCode.ENTER);

        // TODO should the following assert be enabled?
//        assertEquals("Testing!", listView.getItems().get(0));
        assertEquals(1, rt_29650_start_count);
        assertEquals(1, rt_29650_commit_count);
        assertEquals(0, rt_29650_cancel_count);

        sl.dispose();
    }

    private int rt_29849_start_count = 0;
    @Test public void test_rt_29849() {
        installChildren();
        treeTableView.setEditable(true);

        TreeTableColumn<String, String> col = new TreeTableColumn<>("column");
        col.setEditable(true);
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        col.setOnEditStart(t -> {
            rt_29849_start_count++;
        });

        // load the table so the default cells are created
        StageLoader sl = new StageLoader(treeTableView);

        // now replace the cell factory
        Callback<TreeTableColumn<String, String>, TreeTableCell<String, String>> factory = TextFieldTreeTableCell.forTreeTableColumn();
        col.setCellFactory(factory);

        Toolkit.getToolkit().firePulse();

        // now start an edit and count the start edit events - it should be just 1
        treeTableView.edit(0, col);
        assertEquals(1, rt_29849_start_count);

        sl.dispose();
    }

    @Test public void test_rt_34327() {
        // by default the comparator is null.
        // NOTE: this method (prior to the fix as part of RT-34327) would have
        // returned Comparator<String>, but after the fix it correctly returns
        // a Comparator<TreeItem<String>>
        Comparator nonGenericComparator = treeTableView.getComparator();
        Comparator<TreeItem<String>> genericComparator = treeTableView.getComparator();
        assertNull(nonGenericComparator);
        assertNull(genericComparator);

        // add in a column and some data
        TreeTableColumn<String, String> col = new TreeTableColumn<>("column");
        col.setEditable(true);
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));
        treeTableView.getColumns().add(col);

        installChildren();

        // sort by that column
        treeTableView.getSortOrder().add(col);

        // get the new comparator, which should no longer be null
        nonGenericComparator = treeTableView.getComparator();
        genericComparator = treeTableView.getComparator();
        assertNotNull(nonGenericComparator);
        assertNotNull(genericComparator);

        // now, as noted above, previously we would use the Comparator to compare
        // two String instances, which would fail at runtime as the Comparator
        // was actually expecting to compare two TreeItem<String>, but the API
        // was failing us.
        try {
            nonGenericComparator.compare("abc", "def");
            fail("This should not work!");
        } catch (ClassCastException e) {
            // if we get the exception, we're happy
        }

        try {
            Object string1 = "abc";
            Object string2 = "def";
            genericComparator.compare((TreeItem<String>)string1, (TreeItem<String>)string2);
            fail("This should not work!");
        } catch (ClassCastException e) {
            // if we get the exception, we're happy
        }
    }

    @Test public void test_rt26718() {
        treeTableView.setRoot(new TreeItem("Root"));
        treeTableView.getRoot().setExpanded(true);

        for (int i = 0; i < 4; i++) {
            TreeItem parent = new TreeItem("item - " + i);
            treeTableView.getRoot().getChildren().add(parent);

            for (int j = 0; j < 4; j++) {
                TreeItem child = new TreeItem("item - " + i + " " + j);
                parent.getChildren().add(child);
            }
        }

        treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        final TreeItem item0 = treeTableView.getTreeItem(1);
        final TreeItem item1 = treeTableView.getTreeItem(2);

        assertEquals("item - 0", item0.getValue());
        assertEquals("item - 1", item1.getValue());

        item0.setExpanded(true);
        item1.setExpanded(true);
        Toolkit.getToolkit().firePulse();

        treeTableView.getSelectionModel().selectRange(0, 8);
        assertEquals(8, treeTableView.getSelectionModel().getSelectedIndices().size());
        assertEquals(7, treeTableView.getSelectionModel().getSelectedIndex());
        assertEquals(7, treeTableView.getFocusModel().getFocusedIndex());

        // collapse item0 - but because the selected and focused indices are
        // not children of item 0, they should remain where they are (but of
        // course be shifted up). The bug was that focus was moving up to item0,
        // which makes no sense
        item0.setExpanded(false);
        Toolkit.getToolkit().firePulse();
        assertEquals(3, treeTableView.getSelectionModel().getSelectedIndex());
        assertEquals(3, treeTableView.getFocusModel().getFocusedIndex());
    }

    @Ignore("Test started intermittently failing, most probably due to RT-36855 changeset")
    @Test public void test_rt_34493() {
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
            new TreeItem<Person>(new Person("Jacob", "Smith", "jacob.smith@example.com"))
        );

        TreeTableView<Person> table = new TreeTableView<>();

        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);

        TreeTableColumn first = new TreeTableColumn("First Name");
        first.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));

        TreeTableColumn last = new TreeTableColumn("Last Name");
        last.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("lastName"));

        TreeTableColumn email = new TreeTableColumn("Email");
        email.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("email"));

        table.getColumns().addAll(first, last, email);

        // load the table
        StageLoader sl = new StageLoader(table);

        // resize the last column
        last.impl_setWidth(400);
        assertEquals(400, last.getWidth(), 0.0);

        // hide the first column
        table.getColumns().remove(first);
        Toolkit.getToolkit().firePulse();

        // the last column should still be 400px, not the default width or any
        // other value (based on the width of the content in that column)
        assertEquals(400, last.getWidth(), 0.0);

        sl.dispose();
    }

    @Test public void test_rt26721_collapseParent_firstRootChild() {
        TreeTableView<String> table = new TreeTableView<>();
        table.setRoot(new TreeItem("Root"));
        table.getRoot().setExpanded(true);

        for (int i = 0; i < 4; i++) {
            TreeItem parent = new TreeItem("item - " + i);
            table.getRoot().getChildren().add(parent);

            for (int j = 0; j < 4; j++) {
                TreeItem child = new TreeItem("item - " + i + " " + j);
                parent.getChildren().add(child);
            }
        }

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        final TreeItem<String> item0 = table.getTreeItem(1);
        final TreeItem<String> item0child0 = item0.getChildren().get(0);
        final TreeItem<String> item1 = table.getTreeItem(2);

        assertEquals("item - 0", item0.getValue());
        assertEquals("item - 1", item1.getValue());

        item0.setExpanded(true);
        item1.setExpanded(true);
        Toolkit.getToolkit().firePulse();

        // select the first child of item0
        table.getSelectionModel().select(item0child0);

        assertEquals(item0child0, table.getSelectionModel().getSelectedItem());
        assertEquals(item0child0, table.getFocusModel().getFocusedItem());

        // collapse item0 - we expect the selection / focus to move up to item0
        item0.setExpanded(false);
        Toolkit.getToolkit().firePulse();
        assertEquals(item0, table.getSelectionModel().getSelectedItem());
        assertEquals(item0, table.getFocusModel().getFocusedItem());
    }

    @Test public void test_rt26721_collapseParent_lastRootChild() {
        TreeTableView<String> table = new TreeTableView<>();
        table.setRoot(new TreeItem("Root"));
        table.getRoot().setExpanded(true);

        for (int i = 0; i < 4; i++) {
            TreeItem parent = new TreeItem("item - " + i);
            table.getRoot().getChildren().add(parent);

            for (int j = 0; j < 4; j++) {
                TreeItem child = new TreeItem("item - " + i + " " + j);
                parent.getChildren().add(child);
            }
        }

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        final TreeItem<String> item3 = table.getTreeItem(4);
        final TreeItem<String> item3child0 = item3.getChildren().get(0);

        assertEquals("item - 3", item3.getValue());
        assertEquals("item - 3 0", item3child0.getValue());

        item3.setExpanded(true);
        Toolkit.getToolkit().firePulse();

        // select the first child of item0
        table.getSelectionModel().select(item3child0);

        assertEquals(item3child0, table.getSelectionModel().getSelectedItem());
        assertEquals(item3child0, table.getFocusModel().getFocusedItem());

        // collapse item3 - we expect the selection / focus to move up to item3
        item3.setExpanded(false);
        Toolkit.getToolkit().firePulse();
        assertEquals(item3, table.getSelectionModel().getSelectedItem());
        assertEquals(item3, table.getFocusModel().getFocusedItem());
    }

    @Test public void test_rt26721_collapseGrandParent() {
        TreeTableView<String> table = new TreeTableView<>();
        table.setRoot(new TreeItem("Root"));
        table.getRoot().setExpanded(true);

        for (int i = 0; i < 4; i++) {
            TreeItem parent = new TreeItem("item - " + i);
            table.getRoot().getChildren().add(parent);

            for (int j = 0; j < 4; j++) {
                TreeItem child = new TreeItem("item - " + i + " " + j);
                parent.getChildren().add(child);
            }
        }

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        final TreeItem<String> item0 = table.getTreeItem(1);
        final TreeItem<String> item0child0 = item0.getChildren().get(0);
        final TreeItem<String> item1 = table.getTreeItem(2);

        assertEquals("item - 0", item0.getValue());
        assertEquals("item - 1", item1.getValue());

        item0.setExpanded(true);
        item1.setExpanded(true);
        Toolkit.getToolkit().firePulse();

        // select the first child of item0
        table.getSelectionModel().select(item0child0);

        assertEquals(item0child0, table.getSelectionModel().getSelectedItem());
        assertEquals(item0child0, table.getFocusModel().getFocusedItem());

        // collapse root - we expect the selection / focus to move up to root
        table.getRoot().setExpanded(false);
        Toolkit.getToolkit().firePulse();
        assertEquals(table.getRoot(), table.getSelectionModel().getSelectedItem());
        assertEquals(table.getRoot(), table.getFocusModel().getFocusedItem());
    }

    @Test public void test_rt_34685_directEditCall_cellSelectionMode() {
        test_rt_34685_commitCount = 0;
        test_rt_34685(false, true);
    }

    @Test public void test_rt_34685_directEditCall_rowSelectionMode() {
        test_rt_34685_commitCount = 0;
        test_rt_34685(false, false);
    }

    @Test public void test_rt_34685_mouseDoubleClick_cellSelectionMode() {
        test_rt_34685_commitCount = 0;
        test_rt_34685(true, true);
    }

    @Test public void test_rt_34685_mouseDoubleClick_rowSelectionMode() {
        test_rt_34685_commitCount = 0;
        test_rt_34685(true, false);
    }

    private int test_rt_34685_commitCount = 0;
    private void test_rt_34685(boolean useMouseToInitiateEdit, boolean cellSelectionModeEnabled) {
        assertEquals(0, test_rt_34685_commitCount);

        Person person1;
        ObservableList<TreeItem<Person>> persons = FXCollections.observableArrayList(
            new TreeItem<>(person1 = new Person("John", "Smith", "john.smith@example.com"))
        );

        TreeTableView<Person> table = new TreeTableView<>();
        table.getSelectionModel().setCellSelectionEnabled(cellSelectionModeEnabled);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.setEditable(true);

        TreeItem<Person> root = new TreeItem<Person>(new Person("Root", null, null));
        root.setExpanded(true);
        table.setRoot(root);
        table.setShowRoot(false);
        root.getChildren().setAll(persons);

        TreeTableColumn first = new TreeTableColumn("First Name");
        first.setCellValueFactory(new TreeItemPropertyValueFactory<Person, String>("firstName"));
        first.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        EventHandler<TreeTableColumn.CellEditEvent<Person, String>> onEditCommit = first.getOnEditCommit();
        first.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Person, String>>() {
            @Override public void handle(TreeTableColumn.CellEditEvent<Person, String> event) {
                test_rt_34685_commitCount++;
                onEditCommit.handle(event);
            }
        });

        table.getColumns().addAll(first);

        // get the cell at (0,0) - we're hiding the root row
        VirtualFlowTestUtils.BLOCK_STAGE_LOADER_DISPOSE = true;
        TreeTableCell cell = (TreeTableCell) VirtualFlowTestUtils.getCell(table, 0, 0);
        VirtualFlowTestUtils.BLOCK_STAGE_LOADER_DISPOSE = false;
        assertTrue(cell.getSkin() instanceof TreeTableCellSkin);
        assertNull(cell.getGraphic());
        assertEquals("John", cell.getText());
        assertEquals("John", person1.getFirstName());

        // set the table to be editing the first cell at 0,0
        if (useMouseToInitiateEdit) {
            MouseEventFirer mouse = new MouseEventFirer(cell);
            mouse.fireMousePressAndRelease(2, 10, 10);  // click 10 pixels in and 10 pixels down
            mouse.dispose();
        } else {
            table.edit(0,first);
        }

        Toolkit.getToolkit().firePulse();
        assertNotNull(cell.getGraphic());
        assertTrue(cell.getGraphic() instanceof TextField);

        TextField textField = (TextField) cell.getGraphic();
        assertEquals("John", textField.getText());

        textField.setText("Andrew");
        textField.requestFocus();
        Toolkit.getToolkit().firePulse();

        KeyEventFirer keyboard = new KeyEventFirer(textField);
        keyboard.doKeyPress(KeyCode.ENTER);

        VirtualFlowTestUtils.getVirtualFlow(table).requestLayout();
        Toolkit.getToolkit().firePulse();

        VirtualFlowTestUtils.assertTableCellTextEquals(table, 0, 0, "Andrew");
        assertEquals("Andrew", cell.getText());
        assertEquals("Andrew", person1.getFirstName());
        assertEquals(1, test_rt_34685_commitCount);
    }

    @Test public void test_rt34694() {
        TreeItem treeNode = new TreeItem("Controls");
        treeNode.getChildren().addAll(
                new TreeItem("Button"),
                new TreeItem("ButtonBar"),
                new TreeItem("LinkBar"),
                new TreeItem("LinkButton"),
                new TreeItem("PopUpButton"),
                new TreeItem("ToggleButtonBar")
        );

        final TreeTableView<String> table = new TreeTableView<>();
        table.setRoot(treeNode);
        treeNode.setExpanded(true);

        table.getSelectionModel().select(0);
        assertTrue(table.getSelectionModel().isSelected(0));
        assertTrue(table.getFocusModel().isFocused(0));

        treeNode.getChildren().clear();
        treeNode.getChildren().addAll(
                new TreeItem("Button1"),
                new TreeItem("ButtonBar1"),
                new TreeItem("LinkBar1"),
                new TreeItem("LinkButton1"),
                new TreeItem("PopUpButton1"),
                new TreeItem("ToggleButtonBar1")
        );
        Toolkit.getToolkit().firePulse();

        assertTrue(table.getSelectionModel().isSelected(0));
        assertTrue(table.getFocusModel().isFocused(0));
    }

    private int test_rt_35213_eventCount = 0;
    @Test public void test_rt35213() {
        final TreeTableView<String> view = new TreeTableView<>();

        TreeItem<String> root = new TreeItem<>("Boss");
        view.setRoot(root);

        TreeItem<String> group1 = new TreeItem<>("Group 1");
        TreeItem<String> group2 = new TreeItem<>("Group 2");
        TreeItem<String> group3 = new TreeItem<>("Group 3");

        root.getChildren().addAll(group1, group2, group3);

        TreeItem<String> employee1 = new TreeItem<>("Employee 1");
        TreeItem<String> employee2 = new TreeItem<>("Employee 2");

        group2.getChildren().addAll(employee1, employee2);

        TreeTableColumn<String, String> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<String, String>("name"));
        view.getColumns().add(nameColumn);

        view.expandedItemCountProperty().addListener((observableValue, oldCount, newCount) -> {

            // DEBUG OUTPUT
//                System.out.println("new expanded item count: " + newCount.intValue());
//                for (int i = 0; i < newCount.intValue(); i++) {
//                    TreeItem<String> item = view.getTreeItem(i);
//                    String text = item.getValue();
//                    System.out.println("person found at index " + i + " is " + text);
//                }
//                System.out.println("------------------------------------------");

            if (test_rt_35213_eventCount == 0) {
                assertEquals(4, newCount);
                assertEquals("Boss", view.getTreeItem(0).getValue());
                assertEquals("Group 1", view.getTreeItem(1).getValue());
                assertEquals("Group 2", view.getTreeItem(2).getValue());
                assertEquals("Group 3", view.getTreeItem(3).getValue());
            } else if (test_rt_35213_eventCount == 1) {
                assertEquals(6, newCount);
                assertEquals("Boss", view.getTreeItem(0).getValue());
                assertEquals("Group 1", view.getTreeItem(1).getValue());
                assertEquals("Group 2", view.getTreeItem(2).getValue());
                assertEquals("Employee 1", view.getTreeItem(3).getValue());
                assertEquals("Employee 2", view.getTreeItem(4).getValue());
                assertEquals("Group 3", view.getTreeItem(5).getValue());
            } else if (test_rt_35213_eventCount == 2) {
                assertEquals(4, newCount);
                assertEquals("Boss", view.getTreeItem(0).getValue());
                assertEquals("Group 1", view.getTreeItem(1).getValue());
                assertEquals("Group 2", view.getTreeItem(2).getValue());
                assertEquals("Group 3", view.getTreeItem(3).getValue());
            }

            test_rt_35213_eventCount++;
        });

        StageLoader sl = new StageLoader(view);

        root.setExpanded(true);
        Toolkit.getToolkit().firePulse();

        group2.setExpanded(true);
        Toolkit.getToolkit().firePulse();

        group2.setExpanded(false);
        Toolkit.getToolkit().firePulse();

        sl.dispose();
    }

    @Test public void test_rt23245_itemIsInTree() {
        final TreeTableView<String> view = new TreeTableView<String>();
        final List<TreeItem<String>> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final TreeItem<String> item = new TreeItem<String>("Item" + i);
            item.setExpanded(true);
            items.add(item);
        }

        // link the items up so that the next item is the child of the current item
        for (int i = 0; i < 9; i++) {
            items.get(i).getChildren().add(items.get(i + 1));
        }

        view.setRoot(items.get(0));

        for (int i = 0; i < 10; i++) {
            // we expect the level of the tree item at the ith position to be
            // 0, as every iteration we are setting the ith item as the root.
            assertEquals(0, view.getTreeItemLevel(items.get(i)));

            // whilst we are testing, we should also ensure that the ith item
            // is indeed the root item, and that the ith item is indeed the item
            // at the 0th position
            assertEquals(items.get(i), view.getRoot());
            assertEquals(items.get(i), view.getTreeItem(0));

            // shuffle the next item into the root position (keeping its parent
            // chain intact - which is what exposes this issue in the first place).
            if (i < 9) {
                view.setRoot(items.get(i + 1));
            }
        }
    }

    @Test public void test_rt23245_itemIsNotInTree_noRootNode() {
        final TreeView<String> view = new TreeView<String>();
        final List<TreeItem<String>> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final TreeItem<String> item = new TreeItem<String>("Item" + i);
            item.setExpanded(true);
            items.add(item);
        }

        // link the items up so that the next item is the child of the current item
        for (int i = 0; i < 9; i++) {
            items.get(i).getChildren().add(items.get(i + 1));
        }

        for (int i = 0; i < 10; i++) {
            // because we have no root (and we are not changing the root like
            // the previous test), we expect the tree item level of the item
            // in the ith position to be i.
            assertEquals(i, view.getTreeItemLevel(items.get(i)));

            // all items requested from the TreeView should be null, as the
            // TreeView does not have a root item
            assertNull(view.getTreeItem(i));
        }
    }

    @Test public void test_rt23245_itemIsNotInTree_withUnrelatedRootNode() {
        final TreeView<String> view = new TreeView<String>();
        final List<TreeItem<String>> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final TreeItem<String> item = new TreeItem<String>("Item" + i);
            item.setExpanded(true);
            items.add(item);
        }

        // link the items up so that the next item is the child of the current item
        for (int i = 0; i < 9; i++) {
            items.get(i).getChildren().add(items.get(i + 1));
        }

        view.setRoot(new TreeItem("Unrelated root node"));

        for (int i = 0; i < 10; i++) {
            // because we have no root (and we are not changing the root like
            // the previous test), we expect the tree item level of the item
            // in the ith position to be i.
            assertEquals(i, view.getTreeItemLevel(items.get(i)));

            // all items requested from the TreeView should be null except for
            // the root node
            assertNull(view.getTreeItem(i + 1));
        }
    }

    @Test public void test_rt35039_setRoot() {
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        root.getChildren().addAll(
                new TreeItem("aabbaa"),
                new TreeItem("bbc"));

        final TreeTableView<String> treeView = new TreeTableView<>();
        treeView.setRoot(root);

        StageLoader sl = new StageLoader(treeView);

        // everything should be null to start with
        assertNull(treeView.getSelectionModel().getSelectedItem());

        // select "bbc" and ensure everything is set to that
        treeView.getSelectionModel().select(2);
        assertEquals("bbc", treeView.getSelectionModel().getSelectedItem().getValue());

        // change the items list - but retain the same content. We expect
        // that "bbc" remains selected as it is still in the list
        treeView.setRoot(root);
        assertEquals("bbc", treeView.getSelectionModel().getSelectedItem().getValue());

        sl.dispose();
    }

    @Test public void test_rt35039_resetRootChildren() {
        TreeItem aabbaa = new TreeItem("aabbaa");
        TreeItem bbc = new TreeItem("bbc");

        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        root.getChildren().setAll(aabbaa, bbc);

        final TreeTableView<String> treeView = new TreeTableView<>();
        treeView.setRoot(root);

        StageLoader sl = new StageLoader(treeView);

        // everything should be null to start with
        assertNull(treeView.getSelectionModel().getSelectedItem());

        // select "bbc" and ensure everything is set to that
        treeView.getSelectionModel().select(2);
        assertEquals("bbc", treeView.getSelectionModel().getSelectedItem().getValue());

        // change the items list - but retain the same content. We expect
        // that "bbc" remains selected as it is still in the list
        root.getChildren().setAll(aabbaa, bbc);
        assertEquals("bbc", treeView.getSelectionModel().getSelectedItem().getValue());

        sl.dispose();
    }

    @Test public void test_rt35763() {
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        TreeItem aaa = new TreeItem("aaa");
        TreeItem bbb = new TreeItem("bbb");
        root.getChildren().setAll(bbb, aaa);

        final TreeTableView<String> treeView = new TreeTableView<>();

        TreeTableColumn<String, String> col = new TreeTableColumn<>("Column");
        col.setCellValueFactory(param -> param.getValue().valueProperty());

        treeView.getColumns().add(col);
        treeView.setRoot(root);

        assertEquals(root, treeView.getTreeItem(0));
        assertEquals(bbb, treeView.getTreeItem(1));
        assertEquals(aaa,treeView.getTreeItem(2));

        // change sort order - expect items to be sorted
        treeView.getSortOrder().setAll(col);

        assertEquals(1, treeView.getSortOrder().size());
        assertEquals(col, treeView.getSortOrder().get(0));

        Toolkit.getToolkit().firePulse();

        assertEquals(root, treeView.getTreeItem(0));
        assertEquals(bbb, treeView.getTreeItem(2));
        assertEquals(aaa,treeView.getTreeItem(1));

        // set new items into items list - expect sortOrder list to be reset
        // and the items list to remain unsorted
        TreeItem<String> root2 = new TreeItem<>("Root");
        root2.setExpanded(true);
        TreeItem ccc = new TreeItem("ccc");
        TreeItem ddd = new TreeItem("ddd");
        root2.getChildren().setAll(ddd, ccc);
        treeView.setRoot(root2);

        assertEquals(root2, treeView.getTreeItem(0));
        assertEquals(ddd, treeView.getTreeItem(1));
        assertEquals(ccc,treeView.getTreeItem(2));

        assertTrue(treeView.getSortOrder().isEmpty());
    }

    @Test public void test_rt35857() {
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        TreeItem a = new TreeItem("A");
        TreeItem b = new TreeItem("B");
        TreeItem c = new TreeItem("C");
        root.getChildren().setAll(a, b, c);

        final TreeTableView<String> treeTableView = new TreeTableView<String>(root);

        treeTableView.getSelectionModel().select(1);

        ObservableList<TreeItem<String>> selectedItems = treeTableView.getSelectionModel().getSelectedItems();
        assertEquals(1, selectedItems.size());
        assertEquals("A", selectedItems.get(0).getValue());

        root.getChildren().removeAll(selectedItems);
        assertEquals(2, root.getChildren().size());
        assertEquals("B", root.getChildren().get(0).getValue());
        assertEquals("C", root.getChildren().get(1).getValue());
    }

    private int rt36452_instanceCount = 0;
    @Test public void test_rt36452() {
        TreeTableColumn<String, String> myColumn = new TreeTableColumn<String,String>();
        myColumn.setCellValueFactory((item)->(new ReadOnlyObjectWrapper<>(item.getValue().getValue())));
        myColumn.setCellFactory(column -> new TreeTableCell<String, String>() {
            {
                rt36452_instanceCount++;
            }
        });

        TreeTableView<String> ttv = new TreeTableView<>();
        ttv.setShowRoot(false);
        ttv.getColumns().add(myColumn);

        TreeItem<String> treeRootItem = new TreeItem<>("root");
        treeRootItem.setExpanded(true);

        for (int i = 0; i < 100; i++) {
            treeRootItem.getChildren().add(new TreeItem<>("Child: " + i));
        }

        ttv.setRoot(treeRootItem);
        ttv.setFixedCellSize(25);

        StackPane root = new StackPane();
        root.getChildren().add(ttv);

        StageLoader sl = new StageLoader(root);

        final int cellCountAtStart = rt36452_instanceCount;

        // start scrolling
        for (int i = 0; i < 100; i++) {
            ttv.scrollTo(i);
            Toolkit.getToolkit().firePulse();
        }

        // we don't mind if an extra few cells are created. What we are really
        // testing for here is that we don't end up with an order of magnitude
        // extra cells.
        // On my machine the cellCountAtStart is 16. Before this issue was fixed
        // I would end up with 102 instances after running this test. Once the
        // bug was fixed, I would consistently see that 17 cells had been
        // created in total.
        // However, for now, we'll test on the assumption that across all
        // platforms we only get one extra cell created, and we can loosen this
        // up if necessary.
        assertEquals(cellCountAtStart + 1, rt36452_instanceCount);

        sl.dispose();
    }

    @Test public void test_rt25679_rowSelection() {
        test_rt25679(true);
    }

    @Test public void test_rt25679_cellSelection() {
        test_rt25679(false);
    }

    private void test_rt25679(boolean rowSelection) {
        Button focusBtn = new Button("Focus here");

        TreeItem<String> root = new TreeItem<>("Root");
        root.getChildren().setAll(new TreeItem("a"), new TreeItem("b"));
        root.setExpanded(true);

        final TreeTableView<String> treeView = new TreeTableView<>(root);
        TreeTableColumn<String, String> tableColumn = new TreeTableColumn<>();
        tableColumn.setCellValueFactory(rowValue -> new SimpleStringProperty(rowValue.getValue().getValue()));
        treeView.getColumns().add(tableColumn);

        TreeTableView.TreeTableViewSelectionModel<String> sm = treeView.getSelectionModel();
        sm.setCellSelectionEnabled(! rowSelection);

        VBox vbox = new VBox(focusBtn, treeView);

        StageLoader sl = new StageLoader(vbox);
        sl.getStage().requestFocus();
        focusBtn.requestFocus();
        Toolkit.getToolkit().firePulse();

        // test initial state
        assertEquals(sl.getStage().getScene().getFocusOwner(), focusBtn);
        assertTrue(focusBtn.isFocused());
        assertEquals(-1, sm.getSelectedIndex());
        assertNull(sm.getSelectedItem());

        // move focus to the TreeTableView
        treeView.requestFocus();

        // ensure that there is a selection (where previously there was not one)
        assertEquals(sl.getStage().getScene().getFocusOwner(), treeView);
        assertTrue(treeView.isFocused());

        if (rowSelection) {
            assertEquals(1, sm.getSelectedIndices().size());
            assertEquals(root, sm.getSelectedItem());
            assertTrue(sm.isSelected(0));

            assertEquals(1, sm.getSelectedCells().size());
            TreeTablePosition selectedCell = sm.getSelectedCells().get(0);
            assertEquals(0, selectedCell.getRow());
            assertEquals(-1, selectedCell.getColumn());
            assertNull(selectedCell.getTableColumn());
        } else {
            assertTrue(sm.isSelected(0, tableColumn));
            assertEquals(1, sm.getSelectedCells().size());
            TreeTablePosition selectedCell = sm.getSelectedCells().get(0);
            assertEquals(0, selectedCell.getRow());
            assertEquals(0, selectedCell.getColumn());
            assertEquals(tableColumn, selectedCell.getTableColumn());
        }

        sl.dispose();
    }

    @Test public void test_rt36885() {
        test_rt36885(false);
    }

    @Test public void test_rt36885_addChildAfterSelection() {
        test_rt36885(true);
    }

    private void test_rt36885(boolean addChildToAAfterSelection) {
        TreeItem<String> root = new TreeItem<>("Root");         // 0
                TreeItem<String> a = new TreeItem<>("a");       // 1
                    TreeItem<String> a1 = new TreeItem<>("a1"); // a expanded = 2, a collapsed = -1
            TreeItem<String> b = new TreeItem<>("b");           // a expanded = 3, a collapsed = 2
                TreeItem<String> b1 = new TreeItem<>("b1");     // a expanded = 4, a collapsed = 3
                TreeItem<String> b2 = new TreeItem<>("b2");     // a expanded = 5, a collapsed = 4

        root.setExpanded(true);
        root.getChildren().setAll(a, b);

        a.setExpanded(false);
        if (!addChildToAAfterSelection) {
            a.getChildren().add(a1);
        }

        b.setExpanded(true);
        b.getChildren().addAll(b1, b2);

        final TreeTableView<String> treeView = new TreeTableView<>(root);
        TreeTableColumn<String, String> tableColumn = new TreeTableColumn<>();
        tableColumn.setCellValueFactory(rowValue -> new SimpleStringProperty(rowValue.getValue().getValue()));
        treeView.getColumns().add(tableColumn);

        TreeTableView.TreeTableViewSelectionModel<String> sm = treeView.getSelectionModel();
        FocusModel<TreeItem<String>> fm = treeView.getFocusModel();

        sm.select(b1);
        assertEquals(3, sm.getSelectedIndex());
        assertEquals(b1, sm.getSelectedItem());
        assertEquals(3, fm.getFocusedIndex());
        assertEquals(b1, fm.getFocusedItem());

        if (addChildToAAfterSelection) {
            a.getChildren().add(a1);
        }

        a.setExpanded(true);
        assertEquals(4, sm.getSelectedIndex());
        assertEquals(b1, sm.getSelectedItem());
        assertEquals(4, fm.getFocusedIndex());
        assertEquals(b1, fm.getFocusedItem());
    }

    private int rt_37061_index_counter = 0;
    private int rt_37061_item_counter = 0;
    @Test public void test_rt_37061() {
        TreeItem<Integer> root = new TreeItem<>(0);
        root.setExpanded(true);
        TreeTableView<Integer> tv = new TreeTableView<>();
        tv.setRoot(root);
        tv.getSelectionModel().select(0);

        // note we add the listeners after the selection is made, so the counters
        // at this point are still both at zero.
        tv.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            rt_37061_index_counter++;
        });

        tv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            rt_37061_item_counter++;
        });

        // add a new item. This does not impact the selected index or selected item
        // so the counters should remain at zero.
        tv.getRoot().getChildren().add(new TreeItem("1"));
        assertEquals(0, rt_37061_index_counter);
        assertEquals(0, rt_37061_item_counter);
    }

    @Test public void test_rt_37054_noScroll() {
        test_rt_37054(false);
    }

    @Test public void test_rt_37054_scroll() {
        test_rt_37054(true);
    }

    private void test_rt_37054(boolean scroll) {
        ObjectProperty<Integer> offset = new SimpleObjectProperty<Integer>(0);

        // create table with a bunch of rows and 1 column...
        TreeItem<Integer> root = new TreeItem<>(0);
        root.setExpanded(true);
        for (int i = 1; i <= 50; i++) {
            root.getChildren().add(new TreeItem<>(i));
        }

        final TreeTableColumn<Integer, Integer> column = new TreeTableColumn<>("Column");

        final TreeTableView<Integer> table = new TreeTableView<>(root);
        table.getColumns().add( column );
        column.setPrefWidth( 150 );

        // each cell displays x, where x = "cell row number + offset"
        column.setCellValueFactory( cdf -> new ObjectBinding<Integer>() {
            { super.bind( offset ); }

            @Override protected Integer computeValue() {
                return cdf.getValue().getValue() + offset.get();
            }
        });

        StackPane stack = new StackPane();
        stack.getChildren().add(table);
        StageLoader sl = new StageLoader(stack);

        int index = scroll ? 0 : 25;

        if (scroll) {
            // we scroll to force the table cells to update the objects they observe
            table.scrollTo(index);
            Toolkit.getToolkit().firePulse();
        }

        TreeTableCell cell = (TreeTableCell) VirtualFlowTestUtils.getCell(table, index + 3, 0);
        final int initialValue = (Integer) cell.getItem();

        // increment the offset value
        offset.setValue(offset.get() + 1);
        Toolkit.getToolkit().firePulse();

        final int incrementedValue = (Integer) cell.getItem();
        assertEquals(initialValue + 1, incrementedValue);

        sl.dispose();
    }

    private int rt_37395_index_addCount = 0;
    private int rt_37395_index_removeCount = 0;
    private int rt_37395_index_permutationCount = 0;
    private int rt_37395_item_addCount = 0;
    private int rt_37395_item_removeCount = 0;
    private int rt_37395_item_permutationCount = 0;

    @Test public void test_rt_37395() {
        // table items - 3 items, 2nd item has 2 children
        TreeItem<String> root = new TreeItem<>();

        TreeItem<String> two = new TreeItem<>("two");
        two.getChildren().add(new TreeItem<>("childOne"));
        two.getChildren().add(new TreeItem<>("childTwo"));

        root.getChildren().add(new TreeItem<>("one"));
        root.getChildren().add(two);
        root.getChildren().add(new TreeItem<>("three"));

        // table columns - 1 column; name
        TreeTableColumn<String, String> nameColumn = new TreeTableColumn<>("name");
        nameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getValue()));
        nameColumn.setPrefWidth(200);

        // table
        TreeTableView<String> table = new TreeTableView<>();
        table.setShowRoot(false);
        table.setRoot(root);
        table.getColumns().addAll(nameColumn);

        TreeTableView.TreeTableViewSelectionModel sm = table.getSelectionModel();
        sm.getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override public void onChanged(Change<? extends Integer> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        c.getRemoved().forEach(item -> {
                            if (item == null) {
                                fail("Removed index should never be null");
                            } else {
                                rt_37395_index_removeCount++;
                            }
                        });
                    }
                    if (c.wasAdded()) {
                        c.getAddedSubList().forEach(item -> {
                            rt_37395_index_addCount++;
                        });
                    }
                    if (c.wasPermutated()) {
                        rt_37395_index_permutationCount++;
                    }
                }
            }
        });
        sm.getSelectedItems().addListener(new ListChangeListener<TreeItem<String>>() {
            @Override public void onChanged(Change<? extends TreeItem<String>> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        c.getRemoved().forEach(item -> {
                            if (item == null) {
                                fail("Removed item should never be null");
                            } else {
                                rt_37395_item_removeCount++;
                            }
                        });
                    }
                    if (c.wasAdded()) {
                        c.getAddedSubList().forEach(item -> {
                            rt_37395_item_addCount++;
                        });
                    }
                    if (c.wasPermutated()) {
                        rt_37395_item_permutationCount++;
                    }
                }
            }
        });

        assertEquals(0, rt_37395_index_removeCount);
        assertEquals(0, rt_37395_index_addCount);
        assertEquals(0, rt_37395_index_permutationCount);
        assertEquals(0, rt_37395_item_removeCount);
        assertEquals(0, rt_37395_item_addCount);
        assertEquals(0, rt_37395_item_permutationCount);

        StageLoader sl = new StageLoader(table);

        // step one: select item 'three' in index 2
        sm.select(2);
        assertEquals(0, rt_37395_index_removeCount);
        assertEquals(1, rt_37395_index_addCount);
        assertEquals(0, rt_37395_index_permutationCount);
        assertEquals(0, rt_37395_item_removeCount);
        assertEquals(1, rt_37395_item_addCount);
        assertEquals(0, rt_37395_item_permutationCount);

        // step two: expand item 'two'
        // The first part of the bug report was that we received add/remove
        // change events here, when in reality we shouldn't have, so lets enforce
        // that. We do expect a permutation event on the index, as it has been
        // pushed down, but this should not result in an item permutation event,
        // as it remains unchanged
        two.setExpanded(true);
        assertEquals(0, rt_37395_index_removeCount);
        assertEquals(1, rt_37395_index_addCount);
        assertEquals(1, rt_37395_index_permutationCount);
        assertEquals(0, rt_37395_item_removeCount);
        assertEquals(1, rt_37395_item_addCount);
        assertEquals(0, rt_37395_item_permutationCount);

        // step three: collapse item 'two'
        // Same argument as in step two above: no addition or removal, just a
        // permutation on the index
        two.setExpanded(false);
        assertEquals(0, rt_37395_index_removeCount);
        assertEquals(1, rt_37395_index_addCount);
        assertEquals(2, rt_37395_index_permutationCount);
        assertEquals(0, rt_37395_item_removeCount);
        assertEquals(1, rt_37395_item_addCount);
        assertEquals(0, rt_37395_item_permutationCount);

        sl.dispose();
    }

    @Test public void test_rt_37429() {
        // get the current exception handler before replacing with our own,
        // as ListListenerHelp intercepts the exception otherwise
        final Thread.UncaughtExceptionHandler exceptionHandler = Thread.currentThread().getUncaughtExceptionHandler();
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            fail("We don't expect any exceptions in this test!");
        });

        // table items - 3 items, 2nd item has 2 children
        TreeItem<String> root = new TreeItem<>();

        TreeItem<String> two = new TreeItem<>("two");
        two.getChildren().add(new TreeItem<>("childOne"));
        two.getChildren().add(new TreeItem<>("childTwo"));
        two.setExpanded(true);

        root.getChildren().add(new TreeItem<>("one"));
        root.getChildren().add(two);
        root.getChildren().add(new TreeItem<>("three"));

        // table columns - 1 column; name
        TreeTableColumn<String, String> nameColumn = new TreeTableColumn<>("name");
        nameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getValue()));
        nameColumn.setPrefWidth(200);

        // table
        TreeTableView<String> table = new TreeTableView<>();
        table.setShowRoot(false);
        table.setRoot(root);
        table.getColumns().addAll(nameColumn);

        table.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<String>>) c -> {
            while (c.next()) {
                if(c.wasRemoved()) {
                    // The removed list of items must be iterated or the AIOOBE will
                    // not be thrown when getAddedSubList is called.
                    c.getRemoved().forEach(item -> {});
                }

                if (c.wasAdded()) {
                    c.getAddedSubList();
                }
            }
        });

        StageLoader sl = new StageLoader(table);

        table.getSelectionModel().select(0);
        table.getSortOrder().add(nameColumn);

        sl.dispose();

        // reset the exception handler
        Thread.currentThread().setUncaughtExceptionHandler(exceptionHandler);
    }

    private int rt_37429_items_change_count = 0;
    private int rt_37429_cells_change_count = 0;
    @Test public void test_rt_37429_sortEventsShouldNotFireExtraChangeEvents() {
        // table items - 3 items, 2nd item has 2 children
        TreeItem<String> root = new TreeItem<>();

        root.getChildren().add(new TreeItem<>("a"));
        root.getChildren().add(new TreeItem<>("c"));
        root.getChildren().add(new TreeItem<>("b"));

        // table columns - 1 column; name
        TreeTableColumn<String, String> nameColumn = new TreeTableColumn<>("name");
        nameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getValue()));
        nameColumn.setPrefWidth(200);

        // table
        TreeTableView<String> table = new TreeTableView<>();
        table.setShowRoot(false);
        table.setRoot(root);
        table.getColumns().addAll(nameColumn);

        table.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<String>>) c -> {
            while (c.next()) {
                rt_37429_items_change_count++;
            }
        });
        table.getSelectionModel().getSelectedCells().addListener((ListChangeListener<TreeTablePosition<String, ?>>) c -> {
            while (c.next()) {
                rt_37429_cells_change_count++;
            }
        });

        StageLoader sl = new StageLoader(table);

        assertEquals(0, rt_37429_items_change_count);
        assertEquals(0, rt_37429_cells_change_count);

        table.getSelectionModel().select(0);
        assertEquals(1, rt_37429_items_change_count);
        assertEquals(1, rt_37429_cells_change_count);

        table.getSortOrder().add(nameColumn);
        assertEquals(1, rt_37429_items_change_count);
        assertEquals(1, rt_37429_cells_change_count);

        nameColumn.setSortType(TreeTableColumn.SortType.DESCENDING);
        assertEquals(1, rt_37429_items_change_count);
        assertEquals(2, rt_37429_cells_change_count);

        nameColumn.setSortType(TreeTableColumn.SortType.ASCENDING);
        assertEquals(1, rt_37429_items_change_count);
        assertEquals(3, rt_37429_cells_change_count);

        sl.dispose();
    }

    private int rt_37538_count = 0;
    @Test public void test_rt_37538_noCNextCall() {
        test_rt_37538(false, false);
    }

    @Test public void test_rt_37538_callCNextOnce() {
        test_rt_37538(true, false);
    }

    @Test public void test_rt_37538_callCNextInLoop() {
        test_rt_37538(false, true);
    }

    private void test_rt_37538(boolean callCNextOnce, boolean callCNextInLoop) {
        // create table with a bunch of rows and 1 column...
        TreeItem<Integer> root = new TreeItem<>(0);
        root.setExpanded(true);
        for (int i = 1; i <= 50; i++) {
            root.getChildren().add(new TreeItem<>(i));
        }

        final TreeTableColumn<Integer, Integer> column = new TreeTableColumn<>("Column");
        column.setCellValueFactory( cdf -> new ReadOnlyObjectWrapper<Integer>(cdf.getValue().getValue()));

        final TreeTableView<Integer> table = new TreeTableView<>(root);
        table.getColumns().add( column );

        table.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends TreeItem<Integer>> c) -> {
            if (callCNextOnce) {
                c.next();
            } else if (callCNextInLoop) {
                while (c.next()) {
                    // no-op
                }
            }

            if (rt_37538_count >= 1) {
                Thread.dumpStack();
                fail("This method should only be called once");
            }

            rt_37538_count++;
        });

        StageLoader sl = new StageLoader(table);
        assertEquals(0, rt_37538_count);
        table.getSelectionModel().select(0);
        assertEquals(1, rt_37538_count);
        sl.dispose();
    }

    @Test public void test_rt_37593() {
        TreeItem<String> root = new TreeItem<>();

        TreeItem<String> one = new TreeItem<>("one");
        root.getChildren().add(one);

        TreeItem<String> two = new TreeItem<>("two");
        two.getChildren().add(new TreeItem<>("childOne"));
        two.getChildren().add(new TreeItem<>("childTwo"));
        root.getChildren().add(two);

        root.getChildren().add(new TreeItem<>("three"));

        TreeTableColumn<String, String> nameColumn = new TreeTableColumn<>("name");
        nameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getValue()));

        treeTableView.setShowRoot(false);
        treeTableView.setRoot(root);
        treeTableView.getColumns().addAll(nameColumn);

        treeTableView.getSortOrder().add(nameColumn);
        nameColumn.setSortType(TreeTableColumn.SortType.DESCENDING);
        sm.select(one);

        // at this point, the 'one' item should be in row 2
        assertTrue(sm.isSelected(2));
        assertEquals(one, sm.getSelectedItem());

        two.setExpanded(true);

        // this line would create a NPE
        VirtualFlowTestUtils.clickOnRow(treeTableView, 4);

        // we should end up with the selection being on index 4, which is the
        // final location of the 'one' tree item, after sorting and expanding 'two'
        assertEquals(one, sm.getSelectedItem());
        assertTrue(debug(), sm.isSelected(4));
    }
}
