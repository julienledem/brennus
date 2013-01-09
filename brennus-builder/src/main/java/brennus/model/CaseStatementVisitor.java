package brennus.model;

public interface CaseStatementVisitor {

  void visit(CaseBlockStatement caseBlockStatement);

  void visit(GotoCaseStatement gotoCaseStatement);

}
