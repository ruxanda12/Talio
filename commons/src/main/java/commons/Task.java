package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long id;

    public String title;
    public String description;
    public String position;
    public String background;
    public String font;
    public String border;
    public boolean edited;

    @OneToMany(targetEntity = SubTask.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "task_id")
    public List<SubTask> subTasks;

    @ManyToOne
    public FelloList parentFelloList;

    @ManyToMany(targetEntity = Tags.class, fetch = FetchType.EAGER)
    public List<Tags> tags;

    public Task() {
        // for object mappers
    }

    public Task(String title, String description, List<SubTask> subTasks, FelloList parentFelloList,
                String background, String font, String border, boolean edited) {
        this.title = title;
        this.description = description;
        this.subTasks = subTasks;
        this.parentFelloList = parentFelloList;
        this.position = "1";
        this.background = background;
        this.border = border;
        this.font = font;
        this.tags = new ArrayList<>();
        this.edited = edited;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
