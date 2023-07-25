package spring.mvc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.mvc.models.Person;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    public List<Person> index() {
        return jdbcTemplate.query("SELECT * from person", new PersonMapper());
    }
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM PERSON WHERE ID=?",new Object[]{id},new PersonMapper()).
                stream().findAny().orElse(null);
    }
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO PERSON values (1,?,?,?)", person.getName(),person.getAge(),person.getEmail());
    }
    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE PERSON name=?, age=?, email=? WHERE ID=?",updatedPerson.getName(),updatedPerson.getAge(),updatedPerson.getEmail(),id);
    }
    public void delete(int id) {
        jdbcTemplate.update("DELETE from Person where id=?", id);
    }
    public void testMultipleUpdate(){
        List<Person> people= create1000People();
        for (Person person :people){
            jdbcTemplate.update("INSERT INTO Person VALUES (?,?,?,?)", person.getId(),person.getName(),person.getAge(),person.getEmail());
        }
    }
    public void testBatchUpdate(){
        List<Person> people= create1000People();
        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES (?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1,people.get(i).getId());
                        preparedStatement.setString(2,people.get(i).getName());
                        preparedStatement.setInt(3,people.get(i).getAge());
                        preparedStatement.setString(4,people.get(i).getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });
    }
    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();
        for (int i=0;i<1000;i++){
            people.add(new Person(i,"Name"+i,30,"test"+i+"email.ru"));
        }
        return people;
    }
}