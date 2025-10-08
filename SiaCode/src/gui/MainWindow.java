package gui;

import model.Movie;
import model.MovieDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow {
    private final MovieDatabase db;
    private JFrame frame;
    private MyListModel<Movie> listModel;

    public MainWindow(MovieDatabase db) {
        this.db = db;
    }

    public void initAndShow() {
        frame = new JFrame("Movie Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel top = new JPanel(new BorderLayout(6, 6));
        JTextField search = new JTextField();
        JButton btn = new JButton("Поиск");

        JButton sortBtn = new JButton("Каталог Фильмов по рейтингу");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btn);
        buttonPanel.add(sortBtn);

        top.add(search, BorderLayout.CENTER);
        top.add(buttonPanel, BorderLayout.EAST);

        listModel = new MyListModel<>();
        JList<Movie> results = new JList<>(listModel);
        JScrollPane sc = new JScrollPane(results);

        JTextArea details = new JTextArea();
        details.setEditable(false);
        details.setLineWrap(true);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sc, new JScrollPane(details));
        split.setDividerLocation(400);

        frame.getContentPane().add(top, BorderLayout.NORTH);
        frame.getContentPane().add(split, BorderLayout.CENTER);

        ActionListener doSearch = e -> {
            listModel.clear();
            String q = search.getText().trim();
            if (q.isEmpty()) return;
            MovieDatabase.MyArrayList<Movie> r = db.search(q);
            for (int i = 0; i < r.size(); i++) {
                listModel.addElement(r.get(i));
            }
        };
        btn.addActionListener(doSearch);
        search.addActionListener(doSearch);


        sortBtn.addActionListener(e -> {
            listModel.clear();
            MovieDatabase.MyArrayList<Movie> sortedMovies = db.getMoviesSortedByRating();
            for (int i = 0; i < sortedMovies.size(); i++) {
                listModel.addElement(sortedMovies.get(i));
            }
        });

        results.addListSelectionListener(ev -> {
            Movie m = results.getSelectedValue();
            if (m != null) {
                details.setText("Title: " + m.name +
                        "\nGenre: " + m.genre +
                        "\nYear: " + m.year +
                        "\nRating: " + m.rating +
                        "\nScore: " + m.score +
                        "\nVotes: " + m.votes +
                        "\nDirector: " + m.director +
                        "\nWriter: " + m.writer +
                        "\nStar: " + m.star +
                        "\nCountry: " + m.country +
                        "\nCompany: " + m.company +
                        "\nRuntime: " + m.runtime + " min" +
                        "\nBudget: $" + m.budget +
                        "\nGross: $" + m.gross);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        MovieDatabase db = new MovieDatabase("src/movies.json");
        db.init();
        SwingUtilities.invokeLater(() -> new MainWindow(db).initAndShow());
    }
}

class MyListModel<T> extends AbstractListModel<T> {
    private MovieDatabase.MyArrayList<T> data = new MovieDatabase.MyArrayList<>();

    public void addElement(T element) {
        data.add(element);
        fireIntervalAdded(this, data.size() - 1, data.size() - 1);
    }

    public void clear() {
        int oldSize = data.size();
        data = new MovieDatabase.MyArrayList<>();
        if (oldSize > 0) {
            fireIntervalRemoved(this, 0, oldSize - 1);
        }
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public T getElementAt(int index) {
        return data.get(index);
    }
}