package com.example.fmoyader.becomepainter.canvas.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmoyader on 24/5/17.
 */

public class Painting {

    List<Line> lines;

    public Painting() {
        lines = new ArrayList<>();
    }

    public void saveLine(Line line) {
        lines.add(line);

        // draw line
    }

}
