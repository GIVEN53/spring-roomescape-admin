package roomescape.global.query.condition;

import roomescape.global.query.Assemblable;

public class JoinCondition implements Assemblable {
    private final ComparisonCondition condition;

    private JoinCondition(ComparisonCondition condition) {
        this.condition = condition;
    }

    public static JoinCondition on(String column, Object value) {
        return new JoinCondition(ComparisonCondition.equalTo(column, value));
    }

    @Override
    public void assemble(StringBuilder builder) {
        builder.append(" ON ");
        condition.useSingleQuotes(false);
        condition.assemble(builder);
    }
}
