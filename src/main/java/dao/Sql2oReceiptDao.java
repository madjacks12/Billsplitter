package dao;
import models.Receipt;
import models.Item;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;
import java.util.Objects;
public class Sql2oReceiptDao implements ReceiptDao {
    private final Sql2o sql2o;

    public Sql2oReceiptDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Receipt receipt) {
        String sql = "INSERT INTO receipts (receiptName, salestax, cleared) VALUES (:receiptName, :salestax, :cleared)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(receipt)
                    .executeUpdate()
                    .getKey();
            receipt.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Receipt findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM receipts WHERE id=:id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Receipt.class);
        }
    }

    @Override
    public List<Receipt> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM receipts")
                    .executeAndFetch(Receipt.class);
        }
    }

    @Override
    public void update(int id, String receiptName, String salestax, Boolean cleared) {
        String sql = "UPDATE receipts SET (receiptName, salestax, cleared) = (:receiptName, :salestax, :cleared) WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("receiptName", receiptName)
                    .addParameter("salestax", salestax)
                    .addParameter("cleared", cleared)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        try(Connection con = sql2o.open()){
            con.createQuery("DELETE FROM receipts WHERE id=:id")
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM receipts";
        try(Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch(Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
