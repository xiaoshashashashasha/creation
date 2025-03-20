package cn.edu.tust.beauty_back.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean <T> {
    private long total;
    private List<T> items;
}
