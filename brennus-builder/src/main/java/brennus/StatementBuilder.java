package brennus;

import brennus.ExpressionBuilder.ExpressionHandler;
import brennus.SwitchBuilder.SwitchStatementsHandler;
import brennus.model.Expression;
import brennus.model.ExpressionStatement;
import brennus.model.ReturnStatement;
import brennus.model.SetStatement;
import brennus.model.Statement;
import brennus.model.SwitchStatement;
import brennus.model.ThrowStatement;


abstract public class StatementBuilder<T> {

  public interface StatementHandler<T> {
    T handleStatement(Statement statement);
  }

  protected abstract StatementHandler<T> statementHandler();

  public ExpressionBuilder<T> returnExp() {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ReturnStatement(e));
      }
    });
  }

  public ExpressionBuilder<SwitchBuilder<T>> switchOn() {
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

  public ExpressionBuilder<T> throwExp() {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ThrowStatement(e));
      }
    });
  }

  public ExpressionBuilder<T> set(final String to) {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new SetStatement(to, e));
      }
    });
  }

  public ValueExpressionBuilder<T> call(String methodName) {
    return new ExpressionBuilder<T>(new ExpressionHandler<T>() {
      public T handleExpression(Expression e) {
        return statementHandler().handleStatement(new ExpressionStatement(e));
      }
    }).call(methodName);
  }
}
