package org.example;

import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.Label;
import com.indvd00m.ascii.render.elements.Table;
import com.indvd00m.ascii.render.elements.Text;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    IRender render = new Render();
    IContextBuilder builder = render.newBuilder();
    builder.width(37).height(7);
    Table table = new Table(4, 3);
    table.setElement(1, 1, new Label("1234567890"));
    table.setElement(4, 3, new Text("1234567890"), true);
    table.setHighlighted(2, 3, true);
    builder.element(table);
    ICanvas canvas = render.render(builder.build());
    String s = canvas.getText();
    System.out.println(s);
  }
}
