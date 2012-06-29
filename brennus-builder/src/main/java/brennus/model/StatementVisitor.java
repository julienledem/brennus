package brennus.model;

public interface StatementVisitor {

  void visit(ReturnStatement returnStatement);

  void visit(ExpressionStatement methodCallStatement);

  void visit(SwitchStatement switchStatement);

  void visit(CaseStatement caseStatement);

  void visit(ThrowStatement throwStatement);

  void visit(SetStatement setStatement);

}
