package model;


public class Movie {
    public String name;
    public String rating;
    public String genre;
    public int year;
    public String released;
    public double score;
    public long votes;
    public String director;
    public String writer;
    public String star;
    public String country;
    public long budget;
    public long gross;
    public String company;
    public int runtime;

    @Override
    public String toString() {
        return name + " (" + year + ") - " + genre + " - score: " + score;
    }
}