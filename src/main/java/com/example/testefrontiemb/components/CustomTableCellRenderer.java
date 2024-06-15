package com.example.testefrontiemb.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Verifica o valor da coluna "Tipo" (supondo que seja a segunda coluna, índice 1)
        String tipo = (String) table.getValueAt(row, 3);

        if ("Receita".equals(tipo)) {
            setBackground(new Color(183, 225, 205)); // Cor de fundo para "Receita"
        } else if ("Despesa".equals(tipo)) {
            setBackground(new Color(244, 199, 195)); // Cor de fundo para "Despesa"
        } else {
            setBackground(Color.WHITE); // Cor de fundo padrão
        }

        // Se a linha estiver selecionada, mantém a cor de seleção padrão
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setForeground(Color.BLACK); // Cor do texto padrão
        }

        return this;
    }
}