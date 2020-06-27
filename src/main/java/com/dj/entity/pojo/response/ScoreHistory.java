package com.dj.entity.pojo.response;

import com.dj.entity.vo.ScoreVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreHistory {
    private List<ScoreVo> uppers;
    private List<ScoreVo> lowers;

    @Override
    public String toString() {
        return "ScoreHistory{" +
                "uppers=" + uppers +
                ", lowers=" + lowers +
                '}';
    }
}
