package brennus;

import java.util.ArrayList;
import java.util.List;

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
