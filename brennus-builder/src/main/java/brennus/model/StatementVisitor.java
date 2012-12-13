package brennus.model;

/**
 * Visitor pattern for Statements
 *
 * @author Julien Le Dem
 *
 */
public interface StatementVisitor {

  void visit(ReturnStatement returnStatement);

  void visit(ExpressionStatement expressionStatement);

  void visit(SwitchStatement switchStatement);

  void visit(CaseStatement caseStatement);

  void visit(ThrowStatement throwStatement);

  void visit(SetStatement setStatement);

  void visit(IfStatement ifStatement);

  void visit(LabelStatement labelStatement);

  void visit(GotoStatement gotoStatement);

  void visit(CallConstructorStatement callConstructorStatement);

  void visit(DefineVarStatement defineVarStatement);

}
