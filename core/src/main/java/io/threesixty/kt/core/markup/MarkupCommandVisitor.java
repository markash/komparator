package io.threesixty.kt.core.markup;

import org.apache.commons.text.diff.CommandVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MarkupCommandVisitor implements CommandVisitor<Character> {

    public static final String CLASS_INSERT = "diff-insert";
    public static final String CLASS_DELETE = "diff-delete";
    private static final String SPAN_PREFIX = "<span class='";
    private static final String SPAN_POSTFIX = "'>";
    private static final String SPAN_END = "</span>";

    /**
     * A map containing the command and the CSS class
     */
    private Map<Command, String> markUpCommands = new HashMap<>();
    /**
     * The current command
     */
    private Command command = Command.NONE;
    /**
     * The buffer for the diff text
     */
    private StringBuffer buffer = new StringBuffer();

    public MarkupCommandVisitor() {
        this(CLASS_INSERT, CLASS_DELETE);
    }

    public MarkupCommandVisitor(final String insertCssStyle, final String deleteCssStyle) {
        this.markUpCommands.put(Command.DELETE, deleteCssStyle);
        this.markUpCommands.put(Command.INSERT, insertCssStyle);
    }

    public String getMarkup() {
        if (isCurrentMarkupCommand()) {
            visit(Command.END, Optional.empty());
        }

        return this.buffer.toString();
    }

    public void visitInsertCommand(final Character character) {
        visit(Command.INSERT, Optional.of(character));
    }

    public void visitKeepCommand(final Character character) {
        visit(Command.KEEP, Optional.of(character));
    }

    public void visitDeleteCommand(final Character character) {
        visit(Command.DELETE, Optional.of(character));
    }

    private void visit(final Command nextCommand, final Optional<Character> character) {

        if (!isCurrentCommand(nextCommand)) {
                /* Close off the current marked-up command */
            if (isCurrentMarkupCommand()) {
                buffer.append(SPAN_END);
            }
                /* Open the current marked-up command */
            if (isMarkupCommand(nextCommand)) {
                buffer.append(SPAN_PREFIX);
                buffer.append(this.markUpCommands.get(nextCommand));
                buffer.append(SPAN_POSTFIX);
            }
        }
        character.ifPresent(buffer::append);
        command = nextCommand;
    }

    private boolean isMarkupCommand(final Command command) {
        return this.markUpCommands.containsKey(command);
    }

    private boolean isCurrentMarkupCommand() {
        return this.markUpCommands.containsKey(this.command);
    }

    private boolean isCurrentCommand(final Command command) {
        return this.command == command;
    }

    enum Command {
        NONE, INSERT, KEEP, DELETE, END
    }
}