package com.lkx.code.design.jieshiqi;

import java.util.HashMap;
import java.util.Map;

public class ExpressionCase {

    /***
     *
     * @Author ChenjunWang
     * @Description:解释器接口
     * @Date: Created in 16:20 2018/4/17
     * @Modified By:
     *
     */
    public interface Expression {
        int interpreter(Context context);// 一定会有解释方法
    }

    /***
     *
     * @Author ChenjunWang
     * @Description:减法表达式实现类
     * @Date: Created in 16:57 2018/4/17
     * @Modified By:
     *
     */
    public static class MinusOperation extends NonTerminalExpression {

        public MinusOperation(Expression e1, Expression e2) {
            super(e1, e2);
        }

        // 将两个表达式相减
        @Override
        public int interpreter(Context context) {
            return this.e1.interpreter(context) - this.e2.interpreter(context);
        }
    }

    /***
     *
     * @Author ChenjunWang
     * @Description:终结符表达式（在这个例子，用来存放数字，或者代表数字的字符）
     * @Date: Created in 16:22 2018/4/17
     * @Modified By:
     *
     */
    public static class TerminalExpression implements Expression {

        String variable;

        public TerminalExpression(String variable) {

            this.variable = variable;
        }

        @Override
        public int interpreter(Context context) {
            return context.lookup(this);
        }
    }

    /***
     *
     * @Author ChenjunWang
     * @Description:
     * @Date: Created in 16:56 2018/4/17
     * @Modified By:
     *
     */
    public static class PlusOperation extends NonTerminalExpression {

        public PlusOperation(Expression e1, Expression e2) {
            super(e1, e2);
        }

        // 将两个表达式相加
        @Override
        public int interpreter(Context context) {
            return this.e1.interpreter(context) + this.e2.interpreter(context);
        }
    }

    /***
     *
     * @Author ChenjunWang
     * @Description:上下文类（这里主要用来将变量解析成数字【当然一开始要先定义】）
     * @Date: Created in 16:48 2018/4/17
     * @Modified By:
     *
     */
    public static class Context {
        private Map<Expression, Integer> map = new HashMap<>();

        // 定义变量
        public void add(Expression s, Integer value) {
            map.put(s, value);
        }

        // 将变量转换成数字
        public int lookup(Expression s) {
            return map.get(s);
        }
    }

    /***
     *
     * @Author ChenjunWang
     * @Description:测试类
     * @Date: Created in 13:27 2018/4/8
     * @Modified By:
     *
     */
    public static void main(String[] args) {

        Context context = new Context();
        TerminalExpression a = new TerminalExpression("a");
        TerminalExpression b = new TerminalExpression("b");
        TerminalExpression c = new TerminalExpression("c");
        context.add(a, 4);
        context.add(b, 8);
        context.add(c, 2);

        System.out.println(new MinusOperation(new PlusOperation(a, b), c).interpreter(context));
    }

    /***
     *
     * @Author ChenjunWang
     * @Description:抽象非终结符表达式
     * @Date: Created in 16:22 2018/4/17
     * @Modified By:
     *
     */
    public static abstract class NonTerminalExpression implements ExpressionCase.Expression {
        ExpressionCase.Expression e1, e2;

        public NonTerminalExpression(ExpressionCase.Expression e1, ExpressionCase.Expression e2) {
            this.e1 = e1;
            this.e2 = e2;
        }
    }
}
