package br.com.todolist.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeradorDePDF {

    public static void gerarPdf(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados) {
        try (PdfWriter writer = new PdfWriter(nomeArquivo)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adiciona o título
            document.add(new Paragraph(titulo).setFontSize(20).setTextAlignment(TextAlignment.CENTER));

            // Adiciona um espaço
            document.add(new Paragraph("\n"));

            // Adiciona a tabela com os cabeçalhos
            Table table = new Table(cabecalhos.length);
            for (String cabecalho : cabecalhos) {
                table.addHeaderCell(cabecalho);
            }

            // Adiciona os dados
            for (String[] linha : dados) {
                for (String celula : linha) {
                    table.addCell(celula);
                }
            }

            document.add(table);
            document.close();
            System.out.println("PDF gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
        }
    }
}