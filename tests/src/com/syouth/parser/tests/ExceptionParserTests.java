package com.syouth.parser.tests;

import com.syouth.parser.ExpressionParser;
import com.syouth.parser.ParseException;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by syouth on 20.08.15.
 */
public class ExceptionParserTests extends TestCase {

    private ExpressionParser mExpressionParser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mExpressionParser = new ExpressionParser();
    }

    public void testSum() {
        try {
            mExpressionParser.setExpression("2+2");
            Assert.assertEquals(4., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("3 + 3");
            Assert.assertEquals(6., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 3+ 3");
            Assert.assertEquals(6., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 3 +3");
            Assert.assertEquals(6., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void testSubstract() {
        try {
            mExpressionParser.setExpression("2-2");
            Assert.assertEquals(0., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("3 - 3");
            Assert.assertEquals(0., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 3- 3");
            Assert.assertEquals(0., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 3 -3");
            Assert.assertEquals(0., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void  testMult() {
        try {
            mExpressionParser.setExpression("3*4");
            Assert.assertEquals(12., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("3 * 4");
            Assert.assertEquals(12., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 3* 4");
            Assert.assertEquals(12., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 3 *4");
            Assert.assertEquals(12., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void testDiv() {
        try {
            mExpressionParser.setExpression("4/2");
            Assert.assertEquals(2., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("4 / 2");
            Assert.assertEquals(2., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 4/ 2");
            Assert.assertEquals(2., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 4 /2");
            Assert.assertEquals(2., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 2/2/2");
            Assert.assertEquals(0.5, mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void testPower() {
        try {
            mExpressionParser.setExpression("4^2");
            Assert.assertEquals(16., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("4 ^ 2");
            Assert.assertEquals(16., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 4^ 2");
            Assert.assertEquals(16., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 4 ^2");
            Assert.assertEquals(16., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 2 ^-1");
            Assert.assertEquals(1./2., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void testFunctions() {
        try {
            mExpressionParser.setExpression("sqrt2(25)");
            Assert.assertEquals(5., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" sqrt3(27)");
            Assert.assertEquals(3., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" sqrt2( 25 )");
            Assert.assertEquals(5., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" sqrt2( 25) ");
            Assert.assertEquals(5., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" sqrt2(25 ) ");
            Assert.assertEquals(5., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void testComplexExpressions() {
        try {
            mExpressionParser.setExpression("2 +2*2");
            Assert.assertEquals(6., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2 +2*2*sqrt2(24 + 1 )");
            Assert.assertEquals(22., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2^2*3");
            Assert.assertEquals(12., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2^( 2* 3 ) ");
            Assert.assertEquals(64., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2^2 +3");
            Assert.assertEquals(7., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2^(2 +3)");
            Assert.assertEquals(32., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 2 ^2 ^ 3 ");
            Assert.assertEquals(256., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 2 ^ (sqrt2( 25 )) ");
            Assert.assertEquals(32., mExpressionParser.parseExpression());
            mExpressionParser.setExpression(" 2 ^ sqrt2( 25 ) ");
            Assert.assertEquals(32., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("8 - 1 + 8");
            Assert.assertEquals(15., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("8 + - 8");
            Assert.assertEquals(0., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("8 + + 8");
            Assert.assertEquals(16., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2/2*3");
            Assert.assertEquals(3., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2/2*3/3");
            Assert.assertEquals(1., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void testPercents() {
        try {
            mExpressionParser.setExpression("2 + 50% ");
            Assert.assertEquals(3., mExpressionParser.parseExpression());
            /**
             * Not yet implemented properly
             */
            /*mExpressionParser.setExpression("5 + 5 + 50% ");
            Assert.assertEquals(15., mExpressionParser.parseExpression());*/
            mExpressionParser.setExpression("2 * 2 + 50% ");
            Assert.assertEquals(6., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("2 + 50% + 5 ");
            Assert.assertEquals(8., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.assertTrue("Can't parse expression " + e.getMessage(), false);
        }
    }

    public void testVariables() {
        try {
            mExpressionParser.setExpression("x + 1");
            mExpressionParser.setVariable('x', "1");
            Assert.assertEquals(2., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("1 + x");
            mExpressionParser.setVariable('x', "1");
            Assert.assertEquals(2., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("x");
            mExpressionParser.setVariable('x', "1");
            Assert.assertEquals(1., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("x + 3*(x + 1)");
            mExpressionParser.setVariable('x', "1");
            Assert.assertEquals(7., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("x + 3^x");
            mExpressionParser.setVariable('x', "2");
            Assert.assertEquals(11., mExpressionParser.parseExpression());
            mExpressionParser.setExpression("x + 3^(x+1)");
            mExpressionParser.setVariable('x', "2");
            Assert.assertEquals(29., mExpressionParser.parseExpression());
        } catch (ParseException e) {
            Assert.fail("Can't parse expression " + e.getMessage());
        }
    }

    public void testWrongExpressions() {
        try {
            mExpressionParser.setExpression("sqrt (25)");
            Assert.assertEquals(3., mExpressionParser.parseExpression());
            Assert.assertTrue(false);
        } catch (ParseException e) {

        }
    }
}
