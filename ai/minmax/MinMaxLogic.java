package ai.minmax;

import ai.Point;
import main.Board;
import main.Mover;

import java.util.ArrayList;

public abstract class MinMaxLogic {
    MapGraph mapGraph;
    Mover mover;
    Board board;

    public MinMaxLogic(Mover mover, Board board) {
        this.board = board;
        this.mover = mover;
        this.mapGraph = new MapGraph(board.state);
    }

    public abstract int makeMove();

    public abstract ArrayList<Point> getPlannedPath();
}

/*
В лабіринт додати:

    Точки. За кожну точку, що пакман з'їв додавати +1 бал. Якщо пакман з'їв всі точки на карті це означає, що він виграв
    Привидів. Привиди рухають картою і намагаються з'їсти пакмана. Якщо привид з'їв пакмана - програш
    Рівні. З кожним рівнем збільшується складність гри.

Привиди та пакман мають керуватися мінімаксним алгоритмом або альфа-бета відтинанням.

В алгоритм привидів, додати рандомний крок.
*/