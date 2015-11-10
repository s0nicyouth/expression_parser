package com.syouth.parser;

import java.util.HashMap;

/**
 * Created by syouth on 06.08.15.
 */
public class ExpressionParser {
    private CharSequence mExpression;
    private int mCurexpressionPos = -1;
    private HashMap<Character, String> mVarToValMap = new HashMap<>();

    public void setExpression(CharSequence mExpression) {
        this.mExpression = mExpression;
    }

    private Character nextChar() {
        return (++mCurexpressionPos < mExpression.length()) ?
                mExpression.charAt(mCurexpressionPos) :
                null;
    }

    private void eatNChars(int n) {
        for (int i = 0; i < n; i++) {
            nextChar();
        }
    }

    private void eatSpaces() {
        int i = mCurexpressionPos + 1;
        while (i < mExpression.length() && mExpression.charAt(i) == ' ') {
            i++;
        }

        mCurexpressionPos = i - 1;
    }

    public double parseExpression() throws ParseException {
        mCurexpressionPos = -1;
        return parseExpressionInternal();
    }

    /**
     * Grammar:
     * expression = ["+"|"-"] term [{['+'|'-'] expression['%'['+'|'-' expression]]}]
     * term = ['+'|'-']factor ['^' power][{('*'|'/') term['%']}]
     * power = ['+'|'-'](variable | number | '('expression')')['^'power]
     * factor = number | '('expression')' | 'sqrt{digit}('expression')' | variable
     */
    private double parseExpressionInternal() throws ParseException {
        double answer = 0;
        eatSpaces();
        if (checkIfNextDigitPlusOrMinus()) {
            Character nextChar = nextChar();
            if (nextChar != null && nextChar.equals('-')) {
                answer = -1 * parseTerm(null);
            } else {
                answer = parseTerm(null);
            }
        } else {
            answer = parseTerm(null);
        }
        double expressionVal = 0.;
        if (!isOver() &&
                checkIfNextDigitPlusOrMinus()) {
            expressionVal = parseExpressionInternal();
            eatSpaces();
            if (!isOver() && checkIfNextCharOneOf(new char[] {'%'})) {
                nextChar();
                expressionVal = expressionVal * answer / 100;
            }
        }
        eatSpaces();
        if (!isOver() &&
                checkIfNextDigitPlusOrMinus()) {
            expressionVal += parseExpressionInternal();
        }

        return answer + expressionVal;
    }

    private double parsePower() throws ParseException {
        double answer = 0.;
        int sign = 1;
        eatSpaces();
        if (checkIfNextDigitPlusOrMinus()) {
            if (nextChar().equals('-')) {
                sign = -1;
            }
        }
        eatSpaces();
        if (checkIfNextDigitPart()) {
            answer = parseNumber();
        } else if (isNextCharOpeningBracket()) {
            nextChar();
            answer = parseExpressionInternal();
            eatSpaces();
            if (isOver() ||
                    !nextChar().equals(')')) {
                throw new ParseException("No closing bracket at " + mCurexpressionPos);
            }
        } else if (isNextKnownFunction()) {
            answer = parseKnownFunction();
        } else if (isNextCharVariable()) {
            char c = nextChar();
            return Double.valueOf(mVarToValMap.get(c));
        } else  {
            throw new ParseException("Wrong power at " + mCurexpressionPos);
        }
        eatSpaces();
        if (isNextCharPower()) {
            nextChar();
            answer = Math.pow(answer, parsePower());
        }

        return sign * answer;
    }

    private boolean isNextCharOpeningBracket() {
        return checkIfNextCharOneOf(new char[] {'('});
    }

    private boolean isNextCharPower() {
        return checkIfNextCharOneOf(new char[] {'^'});
    }

    private double parseTerm(Double numToDiv) throws ParseException {
        double answer = 0;
        eatSpaces();
        if (isOver()) {
            throw new ParseException("Unexpected end of expression at " + mCurexpressionPos);
        }
        answer = parseFactor();
        eatSpaces();
        if (isNextCharPower()) {
            nextChar();
            answer = Math.pow(answer, parsePower());
        }
        eatSpaces();
        if (numToDiv != null) {
            answer = numToDiv / answer;
        }
        if (chechNextCharMultOrDiv()) {
            switch (nextChar()) {
                case '*':
                    answer *= parseTerm(null);
                    break;
                case '/':
                    answer = parseTerm(answer);
                    break;
            }
        }

        return answer;
    }

    private double parseNumber() throws ParseException {
        eatSpaces();
        if (isOver()) {
            throw new ParseException("Unexpected end Of expression at " + mCurexpressionPos);
        }
        StringBuilder builder = new StringBuilder();

        while (checkIfNextDigitPart() ||
                checkIfNextCharOneOf(new char[] {'.'})) {
            builder.append(nextChar());
        }

        if (builder.length() == 0) {
            throw new ParseException("No valid number at " + mCurexpressionPos);
        }

        return Double.valueOf(builder.toString());
    }

    private boolean isNextCharVariable() {
        int i = mCurexpressionPos + 1;
        if (i >= mExpression.length()) {
            return false;
        }

        return mVarToValMap.containsKey(mExpression.charAt(i));
    }

    private double parseFactor() throws ParseException {
        eatSpaces();
        if (isOver()) {
            throw new ParseException("Unexpected end Of expression at " + mCurexpressionPos);
        }
        int sign = 1;
        if (checkIfNextDigitPlusOrMinus()) {
            if (nextChar().equals('-')) {
                sign = -1;
            }
            eatSpaces();
        }
        if (checkIfNextDigitPart()) {
            return sign * parseNumber();
        } else if (isNextCharOpeningBracket()) {
            nextChar();
            double expr = sign * parseExpressionInternal();
            eatSpaces();
            if (isOver() ||
                    !nextChar().equals(')')) {
                throw new ParseException("No closing bracket at " + mCurexpressionPos);
            } else {
                return expr;
            }
        } else if (isNextKnownFunction()) {
            return sign * parseKnownFunction();
        } else if (isNextCharVariable()) {
            char c = nextChar();
            return Double.valueOf(mVarToValMap.get(c));
        } else {
            throw new ParseException("Unexpected factor at " + mCurexpressionPos);
        }
    }

    private double parseRoot() throws ParseException {
        eatNChars(4);
        double p = parseNumber();
        Character bracket = nextChar();
        if (bracket == null ||
                !bracket.equals('(')) {
            throw new ParseException(
                    "Unexpected symbol at " + mCurexpressionPos + ". ( expected");
        }
        double expr = parseExpressionInternal();
        if (isOver() ||
                !nextChar().equals(')')) {
            throw new ParseException("No closing bracket at " + mCurexpressionPos);
        } else {
            return Math.pow(expr, 1/p);
        }
    }

    private double parseKnownFunction() throws ParseException {
        if (isNextRoot()) {
            return parseRoot();
        } else {
            throw new ParseException("No known function at " + mCurexpressionPos);
        }
    }

    private boolean isNextKnownFunction() {
        return isNextRoot();
    }

    private boolean isNextRoot() {
        int i = mCurexpressionPos + 1;
        if (i >= mExpression.length() || i + 4 >= mExpression.length()) {
            return false;
        }
        return mExpression.subSequence(i, i + 4).
                equals("sqrt");
    }

    private boolean isOver() {
        return mCurexpressionPos + 1 >= mExpression.length();
    }

    private boolean chechNextCharMultOrDiv() {
        return checkIfNextCharOneOf(new char[] {'*', '/'});
    }

    private boolean checkIfNextCharOneOf(char[] chars) {
        int i = mCurexpressionPos + 1;
        if (i >= mExpression.length()) {
            return false;
        }
        char charToChech = mExpression.charAt(i);
        for (char c : chars) {
            if (c == charToChech) {
                return true;
            }
        }

        return false;
    }

    private boolean checkIfNextDigitPart() {
        int i = mCurexpressionPos + 1;
        return (i < mExpression.length() &&
                Character.isDigit(mExpression.charAt(i)));
    }

    private boolean checkIfNextDigitPlusOrMinus() {
        return checkIfNextCharOneOf(new char[] {'+', '-'});
    }

    public void setVariable(char varName, String value) {
        mVarToValMap.put(varName, value);
    }
}
