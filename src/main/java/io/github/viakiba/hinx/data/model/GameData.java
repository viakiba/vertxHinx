package io.github.viakiba.hinx.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="books")
public class GameData {
    @JsonProperty("_id")
    @Id
    private long id;

    private String name;
}
