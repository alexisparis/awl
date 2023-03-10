/*
 * $Id: FilterIssues.java,v 1.9 2007/07/09 13:22:04 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.decorator;

import java.text.Collator;


/**
 * @author Jeanette Winzenburg
 */
public class FilterIssues extends FilterTest {


    
    public void testSortControllerToggleWithComparator() {
        FilterPipeline pipeline = createAssignedPipeline(true);
        SortController controller = pipeline.getSortController();
        controller.toggleSortOrder(0, Collator.getInstance());
        fail("test does nothing - revisit?");
    }

    /**
     * order of filters must be retained.
     *
     */
    public void testSorterOrder() {
        Sorter sorter = new ShuttleSorter(); 
        assertEquals("order < 0", -1, sorter.order);
        Filter[] filters = new Filter[] {sorter, new ShuttleSorter(2, true)};
        new FilterPipeline(filters);
        // JW: pipeline inverts order of sorter - why?
        assertOrders(filters);
    }

    /**
     * trying to find out how much truth is in the api doc
     * for the conversion methods.
     * 
     * As it looks: pipeline _really_ converts all the way between
     * model <--> view, filter converts part of it. Between model and
     * "view" up to the filter.
     *
     */
    public void testConvertRowIndicesToModel() {
        FilterPipeline pipeline = createPipeline();
        Filter intermediateSorter = pipeline.last();
        intermediateSorter.setColumnIndex(2);
        // model contains 21 rows
        pipeline.assign(directModelAdapter);
        // this is 5
        int lastViewRow = pipeline.getOutputSize() - 1;
        // modelrow is 18
        int modelRow = pipeline.convertRowIndexToModel(lastViewRow);
        Filter intermediateFilter1 = pipeline.previous(intermediateSorter);
        Filter intermediateFilter2 = pipeline.previous(intermediateFilter1);
        // first filter has same coordinates as pipeline
        assertEquals(modelRow, intermediateSorter.convertRowIndexToModel(lastViewRow)); 
        // filter previous to sorter has same output as filter
        // not really: was a coincidence that un-/sorted coordiates 
        // on last entry are the same!!
        // so we forced it to be different with setting the column to something else!
        // JW PENDING: need a better test model!
        // fails because expects "view" coordinates in this filter's "view" system!
        assertEquals(modelRow, intermediateFilter1.convertRowIndexToModel(lastViewRow)); 
         assertEquals(modelRow, intermediateFilter2.convertRowIndexToModel(lastViewRow)); 
        
    }

    /**
     * trying to find out how much truth is in the api doc
     * for the conversion methods.
     * 
     * As it looks: pipeline _really_ converts all the way between
     * model <--> view, filter converts part of it (?)
     *
     */
    public void testConvertRowIndicesToView() {
        FilterPipeline pipeline = createPipeline();
        Filter intermediateSorter = pipeline.last();
        intermediateSorter.setColumnIndex(2);
        // model contains 21 rows
        pipeline.assign(directModelAdapter);
        // this is 5
        int lastViewRow = pipeline.getOutputSize() - 1;
        // modelrow is 18
        int modelRow = pipeline.convertRowIndexToModel(lastViewRow);
        Filter intermediateFilter1 = pipeline.previous(intermediateSorter);
        Filter intermediateFilter2 = pipeline.previous(intermediateFilter1);

        // now the other way round
        assertEquals(lastViewRow, pipeline.convertRowIndexToView(modelRow));
        assertEquals(lastViewRow, intermediateSorter.convertRowIndexToView(modelRow));
        // each filter converts from "real" model to it's own "view"
        // so we fail here  
        assertEquals(lastViewRow, intermediateFilter1.convertRowIndexToView(modelRow));
        assertEquals(lastViewRow, intermediateFilter2.convertRowIndexToView(modelRow));
        
    }
    

}
