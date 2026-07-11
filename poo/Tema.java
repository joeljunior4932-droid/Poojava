import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
 
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;


public class Tema {
 
    // cores do modo claro
    public static final Color BG_CLARO = Color.WHITE;
    public static final Color FG_CLARO = Color.BLACK;
 
    // cores do modo escuro
    public static final Color BG_ESCURO = new Color(45, 45, 45);
    public static final Color FG_ESCURO = new Color(230, 230, 230);
    public static final Color CAMPO_BG_ESCURO = new Color(65, 65, 65);
    public static final Color BOTAO_BG_ESCURO = new Color(75, 75, 75);
    public static final Color GRADE_ESCURA = new Color(90, 90, 90);
 
    private Tema() {
    }
 
    public static void aplicar(Component componente, boolean escuro) {
 
        Color bg = escuro ? BG_ESCURO : BG_CLARO;
        Color fg = escuro ? FG_ESCURO : FG_CLARO;
 
        if (componente instanceof JTextComponent) {
 
            componente.setBackground(escuro ? CAMPO_BG_ESCURO : Color.WHITE);
            componente.setForeground(fg);
 
        } else if (componente instanceof JTable) {
 
            JTable tabela = (JTable) componente;
 
            tabela.setBackground(escuro ? CAMPO_BG_ESCURO : Color.WHITE);
            tabela.setForeground(fg);
            tabela.setGridColor(escuro ? GRADE_ESCURA : Color.GRAY);
 
            JTableHeader header = tabela.getTableHeader();
 
            if (header != null) {
                header.setBackground(escuro ? BOTAO_BG_ESCURO
                        : UIManager.getColor("TableHeader.background"));
                header.setForeground(fg);
            }
 
        } else if (componente instanceof JButton
                || componente instanceof JToggleButton) {
 
            componente.setBackground(escuro ? BOTAO_BG_ESCURO
                    : UIManager.getColor("Button.background"));
            componente.setForeground(fg);
 
        } else {
 
            componente.setBackground(bg);
            componente.setForeground(fg);
        }
 
        if (componente instanceof JScrollPane) {
            ((JScrollPane) componente).getViewport()
                    .setBackground(escuro ? CAMPO_BG_ESCURO : Color.WHITE);
        }
 
        if (componente instanceof Container) {
            for (Component filho : ((Container) componente).getComponents()) {
                aplicar(filho, escuro);
            }
        }
    }
}

