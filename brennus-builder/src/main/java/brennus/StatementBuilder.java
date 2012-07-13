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

  final public ExpressionBuilder<T> returnExp() {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ReturnStatement(MethodContext.getSourceLineNumber(), e));
      }
    });
  }

  final public ExpressionBuilder<SwitchBuilder<T>> switchOn() {
    return new ExpressionBuilder<SwitchBuilder<T>>(new ExpressionHandler<SwitchBuilder<T>>() {
      public SwitchBuilder<T> handleExpression(final Expression e) {
        return new SwitchBuilder<T>(e, new SwitchStatementsHandler<T>() {
          public T handleStatement(SwitchStatement switchStatement) {
            return statementHandler().handleStatement(switchStatement);
          }
        });
      }
    });
  }

  final public ExpressionBuilder<T> throwExp() {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ThrowStatement(MethodContext.getSourceLineNumber(), e));
      }
    });
  }

  final public ExpressionBuilder<T> set(final String to) {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new SetStatement(MethodContext.getSourceLineNumber(), to, e));
      }
    });
  }

  final public ExpressionBuilder<ThenBuilder<T>> ifExp() {
    return new ExpressionBuilder<ThenBuilder<T>>(new ExpressionHandler<ThenBuilder<T>>() {
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
  final public MethodCallBuilder<T> call(String methodName) {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression expression) {
        return statementHandler().handleStatement(new ExpressionStatement(MethodContext.getSourceLineNumber(), expression));
      }
    }).call(methodName);
  }
}
