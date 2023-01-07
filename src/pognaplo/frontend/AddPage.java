package pognaplo.frontend;

import pognaplo.kek.Bejegyzes;
import pognaplo.kek.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class AddPage extends JFrame
{
    private JTextField datumTextField;
    private JPanel panel1;
    private JTextField kezdoIdopontTextField;
    private JTextField zaroIdopontTextField;
    private JTextField esemenyTextField;
    private JLabel dateandformat;
    private JButton submitButton;

    public AddPage()
    {
        setIconImage(Controller.ICON.getImage());
        setSize(300, 450);
        setResizable(false);
        setContentPane(panel1);
        submitButton.addActionListener(e ->
        {
            try
            {

                FileWriter fw = new FileWriter(Controller.getFilepath(), true);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
                LocalDate dt = Controller.tryParseDate(datumTextField.getText());
                LocalTime kezdoidopont = LocalTime.parse(kezdoIdopontTextField.getText(), timeFormatter);
                LocalTime zaroidopont = LocalTime.parse(zaroIdopontTextField.getText(), timeFormatter);
                String esemeny = esemenyTextField.getText();




                if (zaroidopont.isBefore(kezdoidopont) || esemeny.length() > 250)
                {
                    throw new Exception();
                }
                if (dt == null) {
                    throw new NullPointerException("");
                }

                if (Controller.naplo.size() == 0)
                {
                    Controller.beolv();
                }
                Bejegyzes b = new Bejegyzes(dt,
                        kezdoidopont,
                        zaroidopont,
                        esemeny,
                        false);
                if (Controller.isUnique(b))
                {
                    Controller.naplo.add(b);
                    fw.write(dt + "," + kezdoidopont + "," + zaroidopont + ',' + esemeny + "\n");
                    fw.close();
                    setVisible(false);
                }

            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            } catch (DateTimeException ex)
            {
                MainWindow.errBox("Rossz Ido volt beadva", "Rossz bemenet");
            } catch (NullPointerException ignored) {
                MainWindow.errBox("Hiba tortent a datum megadasanal", "Hiba tortent");
            } catch (Exception ex)
            {
                MainWindow.errBox("A zaro idopont nem lehet a kezdo idopont elott", "Rossz bemenet");
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 1, new Insets(5, 5, 5, 5), -1, -1));
        datumTextField = new JTextField();
        datumTextField.setText("");
        datumTextField.setToolTipText("yes");
        panel1.add(datumTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Dátum: dd-mm-yyyy");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateandformat = new JLabel();
        dateandformat.setText("kezdo idopont");
        panel1.add(dateandformat, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        kezdoIdopontTextField = new JTextField();
        kezdoIdopontTextField.setText("");
        panel1.add(kezdoIdopontTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("zaro idopont");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zaroIdopontTextField = new JTextField();
        zaroIdopontTextField.setText("");
        panel1.add(zaroIdopontTextField, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        esemenyTextField = new JTextField();
        esemenyTextField.setText("");
        panel1.add(esemenyTextField, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("esemeny");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Submit");
        panel1.add(submitButton, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return panel1;
    }

}
