package duke;

import duke.command.Command;

/**
 * Creates a Duke object that represents a bot that manages a user's tasks.
 */
public class Duke {
    private Ui ui;
    private TaskList tasks;
    private Storage storage;

    /**
     * Creates a duke object.
     * 
     * @param filePath The file path that contains the tasks.
     */
    public Duke(String filePath) { 
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the Duke program.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(this.tasks, this.ui, this.storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Creates a Duke object and runs the program.
     */
    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }
}