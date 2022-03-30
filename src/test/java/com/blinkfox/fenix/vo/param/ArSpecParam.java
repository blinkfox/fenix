package com.blinkfox.fenix.vo.param;

import com.blinkfox.fenix.specification.annotation.Between;
import com.blinkfox.fenix.specification.annotation.Equals;
import com.blinkfox.fenix.specification.annotation.In;
import com.blinkfox.fenix.specification.annotation.Like;
import com.blinkfox.fenix.specification.handler.bean.BetweenValue;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ArSpecParam.
 *
 * @author blinkfox on 2022-03-30.
 * @since 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class ArSpecParam {

    @Equals
    private Long id;

    @Like
    private String name;

    @In("status")
    private List<Integer> states;

    @Between
    private BetweenValue<String> birthday;

}
