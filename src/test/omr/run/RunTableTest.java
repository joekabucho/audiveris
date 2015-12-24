//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                    R u n T a b l e T e s t                                     //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//  Copyright © Herve Bitteur and others 2000-2014. All rights reserved.
//  This software is released under the GNU General Public License.
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.run;

import omr.image.GlobalFilter;

import static omr.run.Orientation.*;

import omr.util.Predicate;

import ij.process.ByteProcessor;

import org.junit.Assert;

import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;

/**
 *
 * @author Hervé Bitteur
 */
public class RunTableTest
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Dimension dim = new Dimension(10, 5);

    //~ Constructors -------------------------------------------------------------------------------
    /**
     * Creates a new {@code RunTableTest} object.
     */
    public RunTableTest ()
    {
    }

    //~ Methods ------------------------------------------------------------------------------------
    /**
     * Test of copy method, of class RunTable.
     */
    @Test
    public void testCopy_0args ()
    {
        System.out.println("\n+++ copy");

        RunTable instance = createHorizontalInstance();
        RunTable expResult = instance;
        RunTable result = instance.copy();

        if (!expResult.isIdentical(result)) {
            fail("Copy not identical to original");
        }
    }

    /**
     * Test of copy method, of class RunTable.
     */
    @Test
    public void testCopy_String ()
    {
        System.out.println("\n+++ copy");

        RunTable instance = createHorizontalInstance();
        RunTable expResult = createHorizontalInstance();
        RunTable result = instance.copy();

        if (!expResult.isIdentical(result)) {
            fail("Copy not identical to original");
        }
    }

    /**
     * Test of dumpOf method, of class RunTable.
     */
    @Test
    public void testDumpOf ()
    {
        System.out.println("\n+++ dumpOf");

        RunTable instance = createHorizontalInstance();
        System.out.println(instance.dumpOf());

        instance = createVerticalInstance();
        System.out.println(instance.dumpOf());
    }

    /**
     * Test of dumpSequences method, of class RunTable.
     */
    @Test
    public void testDumpSequences ()
    {
        System.out.println("\n+++ dumpSequences");

        RunTable instance = createHorizontalInstance();
        instance.dumpSequences();

        instance = createVerticalInstance();
        instance.dumpSequences();
    }

    /**
     * Test of get method, of class RunTable.
     */
    @Test
    public void testGet ()
    {
        System.out.println("\n+++ get");

        RunTable instance = createVerticalInstance();
        assertEquals(255, instance.get(0, 0));
        assertEquals(0, instance.get(1, 0));
        assertEquals(0, instance.get(2, 0));
        assertEquals(255, instance.get(3, 0));
        assertEquals(0, instance.get(0, 1));
        assertEquals(255, instance.get(1, 1));
    }

    /**
     * Test of getBuffer method, of class RunTable.
     */
    @Test
    public void testGetBuffer ()
    {
        System.out.println("\n+++ getBuffer");

        RunTable instance = createVerticalInstance();
        String expResult = "ip[width=10, height=5, bits=8, min=0.0, max=255.0]";
        ByteProcessor result = instance.getBuffer();
        System.out.println("result: " + result);
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of getDimension method, of class RunTable.
     */
    @Test
    public void testGetDimension ()
    {
        System.out.println("\n+++ getDimension");

        RunTable instance = createHorizontalInstance();
        Dimension expResult = new Dimension(10, 5);
        Dimension result = instance.getDimension();
        assertEquals(expResult, result);

        instance = createVerticalInstance();
        result = instance.getDimension();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHeight method, of class RunTable.
     */
    @Test
    public void testGetHeight ()
    {
        System.out.println("\n+++ getHeight");

        RunTable instance = createHorizontalInstance();
        int expResult = dim.height;
        int result = instance.getHeight();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOrientation method, of class RunTable.
     */
    @Test
    public void testGetOrientation ()
    {
        System.out.println("\n+++ getOrientation");

        RunTable instance = createHorizontalInstance();
        Orientation expResult = HORIZONTAL;
        Orientation result = instance.getOrientation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRunAt method, of class RunTable.
     */
    @Test
    public void testGetRunAt ()
    {
        System.out.println("\n+++ getRunAt");

        RunTable instance = createHorizontalInstance();
        Run result = instance.getRunAt(0, 0);
        Run expResult = null;
        assertEquals(expResult, result);

        result = instance.getRunAt(6, 0);
        expResult = new Run(5, 3);
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of getSequence method, of class RunTable.
     */
    @Test
    public void testGetSequence ()
    {
        System.out.println("\n+++ getSequence");

        int index = 1;
        RunTable instance = createHorizontalInstance();
        short[] expResult = new short[]{1, 3, 2};
        System.out.println("expResult: " + Arrays.toString(expResult));

        short[] result = instance.getSequence(index);
        Assert.assertArrayEquals(expResult, result);
    }

    /**
     * Test of getSize method, of class RunTable.
     */
    @Test
    public void testGetSize ()
    {
        System.out.println("\n+++ getSize");

        RunTable instance = createHorizontalInstance();
        int expResult = 5;
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTotalRunCount method, of class RunTable.
     */
    @Test
    public void testGetTotalRunCount ()
    {
        System.out.println("\n+++ getTotalRunCount");

        RunTable instance = createHorizontalInstance();
        int expResult = 9;
        int result = instance.getTotalRunCount();
        assertEquals(expResult, result);

        instance = createVerticalInstance();
        expResult = 16;
        result = instance.getTotalRunCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWidth method, of class RunTable.
     */
    @Test
    public void testGetWidth ()
    {
        System.out.println("\n+++ getWidth");

        RunTable instance = createHorizontalInstance();
        int expResult = dim.width;
        int result = instance.getWidth();
        assertEquals(expResult, result);
    }

    /**
     * Test of include method, of class RunTable.
     */
    @Test
    public void testInclude ()
    {
        System.out.println("\n+++ include");

        RunTable instance = createHorizontalInstance();
        System.out.println("initial:\n" + instance.dumpOf());

        ByteProcessor buffer = instance.getBuffer();
        buffer.invert();

        GlobalFilter filter = new GlobalFilter(buffer, 127);
        RunTableFactory factory = new RunTableFactory(HORIZONTAL);
        RunTable that = factory.createTable(filter.filteredImage());
        System.out.println("inverted:\n" + that.dumpOf());
        that.dumpSequences();

        instance.include(that);
        System.out.println("full:\n" + instance.dumpOf());
        instance.dumpSequences();
        assertEquals(5, instance.getTotalRunCount());
    }

    /**
     * Test of isIdentical method, of class RunTable.
     */
    @Test
    public void testIsIdentical ()
    {
        System.out.println("\n+++ isIdentical");

        RunTable that = createHorizontalInstance();
        RunTable instance = createHorizontalInstance();
        boolean expResult = true;
        boolean result = instance.isIdentical(that);
        assertEquals(expResult, result);

        that = createVerticalInstance();
        expResult = false;
        result = instance.isIdentical(that);
        assertEquals(expResult, result);
    }

    /**
     * Test of purge method, of class RunTable.
     */
    @Test
    public void testPurge_Predicate ()
    {
        System.out.println("\n+++ purge_predicate");

        RunTable instance = createHorizontalInstance();
        Predicate<Run> predicate1 = new Predicate<Run>()
        {
            @Override
            public boolean check (Run run)
            {
                return run.getLength() <= 2;
            }
        };

        RunTable result = instance.purge(predicate1);
        System.out.println("table after1:" + instance.dumpOf());
        instance.dumpSequences();
        assertEquals(2, result.getTotalRunCount());

        Predicate<Run> predicate2 = new Predicate<Run>()
        {
            @Override
            public boolean check (Run run)
            {
                return run.getLength() > 2;
            }
        };

        result = instance.purge(predicate2);
        System.out.println("table after2:" + instance.dumpOf());
        instance.dumpSequences();
        assertEquals(0, result.getTotalRunCount());
    }

    /**
     * Test of purge method, of class RunTable.
     */
    @Test
    public void testPurge_Predicate_RunTable ()
    {
        System.out.println("\n+++ purge_predicate_removed");

        Predicate<Run> predicate1 = new Predicate<Run>()
        {
            @Override
            public boolean check (Run run)
            {
                return run.getLength() == 2;
            }
        };

        RunTable instance = createHorizontalInstance();
        System.out.println("table before1:" + instance.dumpOf());
        instance.dumpSequences();

        RunTable removed = new RunTable(HORIZONTAL, dim.width, dim.height);
        instance.purge(predicate1, removed);
        System.out.println("table after1:" + instance.dumpOf());
        instance.dumpSequences();

        System.out.println("purge after1:" + removed.dumpOf());
        removed.dumpSequences();

        Predicate<Run> predicate2 = new Predicate<Run>()
        {
            @Override
            public boolean check (Run run)
            {
                return run.getLength() == 1;
            }
        };

        instance.purge(predicate2, removed);
        System.out.println("table after2:" + instance.dumpOf());
        instance.dumpSequences();

        System.out.println("purge after2:" + removed.dumpOf());
        removed.dumpSequences();
    }

    /**
     * Test of removeRun method, of class RunTable.
     */
    @Test
    public void testRemoveRun ()
    {
        System.out.println("\n+++ removeRun");

        int pos = 3;
        Run run = new Run(4, 1);
        RunTable instance = createHorizontalInstance();
        short[] seq1 = instance.getSequence(pos);
        System.out.println("sequence before: " + Arrays.toString(seq1));
        System.out.println("table before:" + instance.dumpOf());
        instance.removeRun(pos, run);

        short[] seq2 = instance.getSequence(pos);
        System.out.println("sequence  after: " + Arrays.toString(seq2));
        System.out.println("table after:" + instance.dumpOf());
    }

    /**
     * Test of setSequence method, of class RunTable.
     */
    @Test
    public void testSetSequence ()
    {
        System.out.println("\n+++ setSequence");

        int index = 1;
        short[] seq = new short[]{0, 3, 7};
        RunTable instance = createHorizontalInstance();
        System.out.println("table before:" + instance.dumpOf());
        instance.setSequence(index, seq);
        System.out.println("table after:" + instance.dumpOf());
    }

    /**
     * Test of toString method, of class RunTable.
     */
    @Test
    public void testToString ()
    {
        System.out.println("\n+++ toString");

        RunTable instance = createHorizontalInstance();
        String expResult = "RunTable{HORIZONTAL 10x5}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    //-------------------------------//
    // createLargeHorizontalInstance //
    //-------------------------------//
    private RunTable createLargeHorizontalInstance ()
    {
        RunTable instance = new RunTable(HORIZONTAL, dim.width + 5, dim.height + 3);

        int i = 2;
        instance.addRun(i, new Run(1, 2));
        instance.addRun(i, new Run(5, 3));

        i++;
        instance.addRun(i, new Run(4, 2));

        i++;
        i++;
        instance.addRun(i, new Run(4, 1));
        instance.addRun(i, new Run(8, 2));

        i++;
        instance.addRun(i, new Run(2, 2));
        instance.addRun(i, new Run(6, 4));

        return instance;
    }

    /**
     * Test of trim method, of class RunTable.
     */
    @Test
    public void testTrim ()
    {
        System.out.println("\n+++ trim");

        RunTable instance = createLargeHorizontalInstance();
        System.out.println("beforeTrim:\n" + instance.dumpOf());

        Point expOffset = new Point(1, 2);
        Point offset = new Point();
        RunTable trimmed = instance.trim(offset);
        System.out.println("afterTrim:\n" + trimmed.dumpOf());

        assertEquals(expOffset, offset);
        assertEquals(9, trimmed.getWidth());
        assertEquals(5, trimmed.getHeight());
    }

    //--------------------------//
    // createHorizontalInstance //
    //--------------------------//
    private RunTable createHorizontalInstance ()
    {
        RunTable instance = new RunTable(HORIZONTAL, dim.width, dim.height);

        instance.addRun(0, new Run(1, 2));
        instance.addRun(0, new Run(5, 3));

        instance.addRun(1, new Run(0, 1));
        instance.addRun(1, new Run(4, 2));

        instance.addRun(3, new Run(0, 2));
        instance.addRun(3, new Run(4, 1));
        instance.addRun(3, new Run(8, 2));

        instance.addRun(4, new Run(2, 2));
        instance.addRun(4, new Run(6, 4));

        ///System.out.println("createHorizontalInstance:\n" + instance.dumpOf());
        return instance;
    }

    //------------------------//
    // createVerticalInstance //
    //------------------------//
    private RunTable createVerticalInstance ()
    {
        RunTable hori = createHorizontalInstance();
        ByteProcessor buffer = hori.getBuffer();
        GlobalFilter filter = new GlobalFilter(buffer, 127);
        RunTableFactory factory = new RunTableFactory(VERTICAL);

        RunTable instance = factory.createTable(filter.filteredImage());

        ///System.out.println("createVerticalInstance:\n" + instance.dumpOf());
        return instance;
    }
}