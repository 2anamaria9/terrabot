import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

public class CheckstyleAuditListener implements AuditListener {
    private final StringBuilder errors = new StringBuilder();

    @Override
    public void auditStarted(AuditEvent auditEvent) {
    }

    @Override
    public void auditFinished(AuditEvent auditEvent) {
    }

    @Override
    public void fileStarted(AuditEvent auditEvent) {
    }

    @Override
    public void fileFinished(AuditEvent auditEvent) {
    }

    @Override
    public void addError(AuditEvent auditEvent) {
        errors.append(auditEvent.getFileName())
                .append(":")
                .append(auditEvent.getLine())
                .append(" - ")
                .append(auditEvent.getMessage())
                .append("\n");
    }

    @Override
    public void addException(AuditEvent auditEvent, Throwable throwable) {
    }

    @Override
    public String toString() {
        return errors.toString();
    }
}
