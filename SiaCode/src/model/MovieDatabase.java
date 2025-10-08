package model;

import java.io.*;

public class MovieDatabase {
    private final String filePath;
    public MyArrayList<Movie> movies;

    public MovieDatabase(String filePath) {
        this.filePath = filePath;
        this.movies = new MyArrayList<>();
    }

    public void init() throws IOException {
        StringBuilder json = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line.trim());
            }
        }

        String all = json.toString();
        all = all.substring(1, all.length() - 1);
        String[] objects = all.split("\\},\\{");

        for (String obj : objects) {
            obj = obj.replace("{", "").replace("}", "");
            Movie m = parseMovie(obj);
            if (m != null) movies.add(m);
        }
    }

    private Movie parseMovie(String obj) {
        Movie m = new Movie();
        String[] fields = obj.split(",\"");

        for (String f : fields) {
            f = f.replace("\"", "").trim();
            String[] kv = f.split(":", 2);
            if (kv.length < 2) continue;
            String key = kv[0].trim();
            String value = kv[1].trim();

            try {
                switch (key) {
                    case "name": m.name = value; break;
                    case "rating": m.rating = value; break;
                    case "genre": m.genre = value; break;
                    case "year": m.year = Integer.parseInt(value); break;
                    case "released": m.released = value; break;
                    case "score": m.score = Double.parseDouble(value); break;
                    case "votes": m.votes = Long.parseLong(value); break;
                    case "director": m.director = value; break;
                    case "writer": m.writer = value; break;
                    case "star": m.star = value; break;
                    case "country": m.country = value; break;
                    case "budget": m.budget = Long.parseLong(value); break;
                    case "gross": m.gross = Long.parseLong(value); break;
                    case "company": m.company = value; break;
                    case "runtime": m.runtime = Integer.parseInt(value); break;
                }
            } catch (Exception e) {
            }
        }
        return m;
    }

    public MyArrayList<Movie> search(String q) {
        q = q.toLowerCase();
        MyArrayList<Movie> result = new MyArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            if ((m.name != null && m.name.toLowerCase().contains(q)) ||
                    (m.genre != null && m.genre.toLowerCase().contains(q)) ||
                        (m.company != null && m.company.toLowerCase().contains(q)) ||
                            (m.director != null && m.director.toLowerCase().contains(q)))
            {
                result.add(m);
                if (result.size() >= 200) break;
            }
        }
        return result;
    }

      public MyArrayList<Movie> getMoviesSortedByRating() {
        MyArrayList<Movie> sortedMovies = new MyArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            sortedMovies.add(movies.get(i));
        }

        bubbleSortByRating(sortedMovies);
        return sortedMovies;
    }
    private void bubbleSortByRating(MyArrayList<Movie> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Movie m1 = list.get(j);
                Movie m2 = list.get(j + 1);

                if (m1.score < m2.score) {

                    Object temp = list.elements[j];
                    list.elements[j] = list.elements[j + 1];
                    list.elements[j + 1] = temp;
                }
            }
        }
    }

    public static class MyArrayList<T> {
        private static final int DEFAULT_CAPACITY = 10;
        Object[] elements;
        private int size;

        public MyArrayList() {
            this.elements = new Object[DEFAULT_CAPACITY];
            this.size = 0;
        }

        public void add(T element) {
            ensureCapacity(size + 1);
            elements[size++] = element;
        }

        @SuppressWarnings("unchecked")
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return (T) elements[index];
        }

        public int size() {
            return size;
        }

        private void ensureCapacity(int minCapacity) {
            if (minCapacity > elements.length) {
                int newCapacity = elements.length * 2;
                if (newCapacity < minCapacity) {
                    newCapacity = minCapacity;
                }
                elements = manualArrayCopy(newCapacity);
            }
        }

        private Object[] manualArrayCopy(int newCapacity) {
            Object[] newElements = new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }

            return newElements;
        }

        public int getCapacity() {
            return elements.length;
        }
    }
}