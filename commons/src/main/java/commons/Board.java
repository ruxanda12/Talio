package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Board{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long id;

    public String title;
    public boolean prot;
    public String password;
    public String background;
    public String font;
    public String border;
    public String key;

    public String listBackground;
    public String listFont;
    public String listBorder;

    @OneToMany(targetEntity = FelloList.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    public List<FelloList> felloLists;

    @OneToMany(targetEntity = Tags.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    public List<Tags> tags;

    @OneToMany(targetEntity = Palette.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "board_id")
    public List<Palette> palettes;

    @SuppressWarnings("unused")
    public Board() {
        // for object mappers
    }

    public Board(String title, boolean prot, String password, List<FelloList> felloLists, List<Tags> tags,
                 List<Palette> palettes, String background, String font, String border, String listBackground,
                 String listFont, String listBorder) {
        this.title = title;
        this.prot = prot;
        this.password = password;
        this.felloLists = felloLists;
        this.tags = tags;
        this.palettes = palettes;
        this.background = background;
        this.font = font;
        this.border = border;
        this.listBackground = listBackground;
        this.listFont = listFont;
        this.listBorder = listBorder;
        int random = (int) ((Math.random() * Integer.MAX_VALUE) % 100000);
        this.key = String.valueOf(Math.abs(title.hashCode() + random)).substring(0,
                Math.min(9, String.valueOf(Math.abs(title.hashCode() + random)).length()));
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

    public String getTitle() {
        return title;
    }
}
