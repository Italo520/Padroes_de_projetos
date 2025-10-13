// Em: src/main/java/br/com/todolist/Main.java
package br.com.todolist;

import br.com.todolist.ui.controllers_ui.telausuario.TelaLoginController;
import br.com.todolist.ui.views.telasusuario.TelaLoginView;
import javax.swing.SwingUtilities;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

public class Main {
    public static void main(String[] args) {

        FlatCarbonIJTheme.setup();

        SwingUtilities.invokeLater(() -> {
            TelaLoginView view = new TelaLoginView();
            new TelaLoginController(view); // O controller se anexa à view
        });
    }
}