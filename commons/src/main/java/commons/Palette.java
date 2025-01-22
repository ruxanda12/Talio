package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Palette{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long id;

    public String title;
    public boolean isDefault;

    public String background;
    public String font;
    public String border;

    @ManyToOne
    public Board parentBoard;

    @SuppressWarnings("unused")
    public Palette() {
        // for object mappers
    }

    public Palette(Board parentBoard, String title, boolean isDefault,
                   String background, String font, String border) {
        this.parentBoard = parentBoard;
        this.title = title;
        this.isDefault = isDefault;
        this.background = background;
        this.font = font;
        this.border = border;
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
