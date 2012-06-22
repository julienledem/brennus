package brennus;

import java.util.ArrayList;
import java.util.List;

import brennus.model.Statement;

/**
 * builds a block of code containing statements
 *
 * @author Julien Le Dem
 *
 * @param <S> the type of statement accepted in this block
 */
abstract public class CodeBlockBuilder<S extends Statement> {

  private final List<S> statements = new ArrayList<S>();

  public CodeBlockBuilder() {
    super();
  }

  void addStatement(S statement) {
    statements.add(statement);
  }

  public List<S> getStatements() {
    return statements;
  }

}
