package liquidjava.rj_language.parsing;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import rj.grammar.RJLexer;
import rj.grammar.RJParser;

public class RJListener implements ParseTreeListener {

    @Override
    public void visitTerminal(TerminalNode node) {
        int ti = node.getSymbol().getType();
        if (ti < 0)
            System.out.println(node.getSymbol());
        else {
            String i = RJLexer.ruleNames[ti - 1];
            System.out.println("Found literal:" + node.getText());
            System.out.println("Type:" + i);
            System.out.println("....");
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        int ruleIndex = ctx.getRuleIndex();
        String ruleName = RJParser.ruleNames[ruleIndex];

        System.out.println("Non-Terminal: " + ctx.getText());
        System.out.println("Type of Token:" + ruleName);
        System.out.println("....");

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        // TODO Auto-generated method stub

    }

}
