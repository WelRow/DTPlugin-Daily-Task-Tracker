package com.ksaifstack.docktask.plugins.firstparty;

import com.ksaifstack.docktask.plugins.MenuPlugin;
import com.ksaifstack.docktask.plugins.PluginContext;
import com.ksaifstack.docktask.plugins.WidgetPlugin;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import java.time.LocalDate;

public class TaskCounterPlugin implements MenuPlugin, WidgetPlugin {

    private int tasksCompleted = 0;
    private int dailyGoal = 5;
    private Label counterLabel;

    @Override
    public String getName() { return "Daily Task Tracker"; }

    @Override
    public String getVersion() { return "1.0.0"; }

    @Override
    public String getDescription() { return "Track daily task completions with visual color cues. Set your goal in the Plugin Hub!"; }

    @Override
    public Region getWidgetContent(PluginContext context) {
        handleDailyReset(context);

        // load state
        tasksCompleted = Integer.parseInt(context.loadState("tasks_completed", "0"));
        dailyGoal = Integer.parseInt(context.loadState("daily_goal", "5"));

        // build ui
        VBox box = new VBox(10);
        context.loadPluginCss(box, "/plugins/firstparty/taskcounter.css"); //
        box.getStyleClass().add("counter-widget");
        box.setPrefSize(160, 140);

        Label title = new Label("Tasks Done");
        title.setFont(context.getFont(14)); //
        title.getStyleClass().add("counter-text");

        counterLabel = new Label();
        counterLabel.setFont(context.getFont(36));
        updateCounterDisplay(); // Set initial text and color

        Button addBtn = new Button("+1 Task");
        Button resetBtn = new Button("Reset");

        addBtn.setOnAction(e -> {
            tasksCompleted++;
            context.saveState("tasks_completed", String.valueOf(tasksCompleted));
            updateCounterDisplay();
        });

        resetBtn.setOnAction(e -> {
            tasksCompleted = 0;
            context.saveState("tasks_completed", "0");
            updateCounterDisplay();
        });

        box.getChildren().addAll(title, counterLabel, addBtn, resetBtn);
        return box;
    }

    @Override
    public Pane getMenuContent(PluginContext context) {
        dailyGoal = Integer.parseInt(context.loadState("daily_goal", "5"));

        VBox menuBox = new VBox(15);
        context.loadPluginCss(menuBox, "/plugins/firstparty/taskcounter.css");
        menuBox.getStyleClass().add("counter-menu");

        Label title = new Label(getName() + " Settings");
        title.setFont(context.getFont(24));
        title.getStyleClass().add("counter-text");

        Label goalLabel = new Label("Set Daily Goal:");
        goalLabel.setFont(context.getFont(14));
        goalLabel.getStyleClass().add("counter-text");

        TextField goalInput = new TextField(String.valueOf(dailyGoal));
        goalInput.setMaxWidth(100);

        Button saveBtn = new Button("Save Goal");
        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("counter-text");

        // save goal
        saveBtn.setOnAction(e -> {
            try {
                int newGoal = Integer.parseInt(goalInput.getText());
                if (newGoal > 0) {
                    context.saveState("daily_goal", String.valueOf(newGoal));
                    dailyGoal = newGoal;
                    statusLabel.setText("Goal saved! Restart widget to apply.");
                } else {
                    statusLabel.setText("Goal must be > 0.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid number.");
            }
        });

        menuBox.getChildren().addAll(title, goalLabel, goalInput, saveBtn, statusLabel);
        return menuBox;
    }

    // reset the counter to 0 if a new day has started
    private void handleDailyReset(PluginContext context) {
        String savedDateStr = context.loadState("last_active_date", "");
        String currentDateStr = LocalDate.now().toString();

        if (!currentDateStr.equals(savedDateStr)) {
            context.saveState("tasks_completed", "0");
            context.saveState("last_active_date", currentDateStr);
        }
    }

    // Red-to-Green color shift as task number goes up
    private void updateCounterDisplay() {
        counterLabel.setText(tasksCompleted + " / " + dailyGoal);


        double percent = Math.min(1.0, (double) tasksCompleted / dailyGoal);

        int r = (int) (255 - (195 * percent));
        int g = (int) (60 + (195 * percent));
        int b = 60;

        String colorHex = String.format("#%02x%02x%02x", r, g, b);
        counterLabel.setStyle("-fx-text-fill: " + colorHex + "; -fx-font-weight: bold;");
    }

    @Override public double getDefaultX()    { return 100; }
    @Override public double getDefaultY()    { return 100; }
    @Override public double getDefaultSize() { return 160; }
}