package web.group05.trentoticketing.Data;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String nome;
    private String cognome;
    private Date data_nascita;
    private String email;
    private String telefono;
    private String username;
    private boolean is_admin;
    private int biglietti_acquistati;

    public User() { }
    public User(String n, String c, Date d, String e, String t, String u, boolean i, int b) {
        nome = n;
        cognome = c;
        data_nascita = d;
        email = e;
        telefono = t;
        username = u;
        is_admin = i;
        biglietti_acquistati = b;
    }

    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public Date getData_nascita() { return data_nascita; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getUsername() { return username; }
    public boolean getIs_admin() { return is_admin; }
    public int getBiglietti_aquistati() { return biglietti_acquistati; }
    public void acqista_biglietti(int ba) { biglietti_acquistati += ba; }

    // non so se voglio avere dei setter, dato che l'unico modo di modificare un user è agendo sul DB con una query
}
