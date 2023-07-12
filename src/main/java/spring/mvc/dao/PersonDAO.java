package spring.mvc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.mvc.models.Person;

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
}