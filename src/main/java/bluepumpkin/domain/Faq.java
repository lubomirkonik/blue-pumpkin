package bluepumpkin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Lubosh
 */
@Entity(name = "faq")
public class Faq {

    @Id
    @Column(name = "ID")
    private String id;

    private String question;
    private String answer;
    
    public Faq() {
    }

    public Faq(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "bluepumpkin.domain.Faq[ id=" + id + " ]";
    }
    
}

