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


abstract public class StatementBuilder<T> {

  public interface StatementHandler<T> {
    T handleStatement(Statement statement);
  }

  /**
   * @return the statement handler to be called with the finished statement
   */
  protected abstract StatementHandler<T> statementHandler();

  final public ExpressionBuilder<T, ReturnValueExpressionBuilder<T>> returnExp() {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new ReturnExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ReturnStatement(sourceLineNumber, e));
      }
    });
  }

  final public ExpressionBuilder<SwitchBuilder<T>, SwitchValueExpressionBuilder<T>> switchOn() {
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

  final public ExpressionBuilder<T, ThrowValueExpressionBuilder<T>> throwExp() {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new ThrowExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ThrowStatement(sourceLineNumber, e));
      }
    });
  }

  final public ExpressionBuilder<T, SetValueExpressionBuilder<T>> set(final String to) {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new SetExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new SetStatement(sourceLineNumber, to, e));
      }
    });
  }

  final public ExpressionBuilder<ThenBuilder<T>, ThenValueExpressionBuilder<T>> ifExp() {
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
   * creates a call statement
   * @param methodName
   * @return a methodCallBuilder for optionally passing parameters
   */
  final public ExpressionBuilder<T, ExecValueExpressionBuilder<T>> exec() {
    final int sourceLineNumber = MethodContext.getSourceLineNumber();
    return new ExecExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ExpressionStatement(sourceLineNumber, e));
      }
    });
  }
}
