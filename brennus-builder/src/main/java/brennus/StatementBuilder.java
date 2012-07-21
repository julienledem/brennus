package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.ThenBuilder.IfStatementHandler;
import brennus.SwitchBuilder.SwitchStatementsHandler;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.IfStatement;
import brennus.model.ReturnStatement;
import brennus.model.SetStatement;
import brennus.model.Statement;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;

/**
 * statement builder
 *
 * @author Julien Le Dem
 *
 * @param <T> the type of the parent to be returned on completion
 */
abstract public class StatementBuilder<T> {

  interface StatementHandler<T> {
    T handleStatement(Statement statement);
  }

  /**
   * @return the statement handler to be called with the finished statement
   */
  protected abstract StatementHandler<T> statementHandler();

  /**
   * returnExp().{expression}.endReturn()
   * @return an expression builder
   */
  final public ReturnExpressionBuilder<T> returnExp() {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new ReturnExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ReturnStatement(sourceLineNumber, e));
      }
    });
  }

  /**
   * switchOn().{expression}.switchBlock()
   *   .caseBlock(int)
   *   {statements}
   *   .[end|break]Case()
   *   .defaultCase()
   *   {statements}
   *   .[end|break]Case()
   * .endSwitch()
   * @return an expression builder
   */
  final public SwitchExpressionBuilder<T> switchOn() {
    return new SwitchExpressionBuilder<T>(new ExpressionHandler<SwitchBuilder<T>>() {
      public SwitchBuilder<T> handleExpression(final Expression e) {
        return new SwitchBuilder<T>(e, new SwitchStatementsHandler<T>() {
          public T handleStatement(SwitchStatement switchStatement) {
            return statementHandler().handleStatement(switchStatement);
          }
        });
      }
    });
  }

  /**
   * throwExp().{expression}.endThrow()
   * @return an expression builder
   */
  final public ThrowExpressionBuilder<T> throwExp() {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new ThrowExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ThrowStatement(sourceLineNumber, e));
      }
    });
  }

  /**
   * set(varName).{expression}.endSet()
   * @return an expression builder
   */
  final public SetExpressionBuilder<T> set(final String to) {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new SetExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new SetStatement(sourceLineNumber, to, e));
      }
    });
  }

  /**
   * ifExp().{expression}.thenBlock()
   * {statements}
   * .elseBlock()
   * {statements}
   * .endIf()
   * @return an expression builder
   */
  final public ThenExpressionBuilder<T> ifExp() {
    return new ThenExpressionBuilder<T>(new ExpressionHandler<ThenBuilder<T>>() {
      public ThenBuilder<T> handleExpression(final Expression e) {
        return new ThenBuilder<T>(e, new IfStatementHandler<T>() {
          public T handleStatement(IfStatement ifStatement) {
            return statementHandler().handleStatement(ifStatement);
          }
        });
      }
    });
  }

  /**
   * exec().{expression}.endExec()
   * @return an expression builder
   */
  final public ExecExpressionBuilder<T> exec() {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new ExecExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ExpressionStatement(sourceLineNumber, e));
      }
    });
  }
}
