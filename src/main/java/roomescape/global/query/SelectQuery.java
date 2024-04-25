package roomescape.global.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import roomescape.global.query.condition.ComparisonCondition;
import roomescape.global.query.condition.JoinCondition;
import roomescape.global.query.condition.LogicalCondition;

public class SelectQuery extends Query {
    private final String table;
    private final List<String> columns;
    private final LogicalCondition condition;
    private Join join = Join.EMPTY;
    private String alias;

    protected SelectQuery(String table) {
        Objects.requireNonNull(table, "테이블명은 필수입니다.");
        this.table = table;
        this.columns = new ArrayList<>();
        this.condition = LogicalCondition.and();
    }

    public SelectQuery addColumns(String... columnNames) {
        this.columns.addAll(Arrays.asList(columnNames));
        return this;
    }

    public SelectQuery addAllColumns() {
        this.columns.add("*");
        return this;
    }

    public SelectQuery where(ComparisonCondition condition) {
        this.condition.addCondition(condition);
        return this;
    }

    public SelectQuery alias(String alias) {
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("alias가 비어있습니다.");
        }
        this.alias = alias;
        return this;
    }

    public SelectQuery join(String joinType, String joinTable, JoinCondition joinCondition, String alias) {
        join = new Join(joinType, joinTable, joinCondition, alias);
        return this;
    }

    @Override
    public void assemble(StringBuilder builder) {
        if (columns.isEmpty()) {
            throw new IllegalArgumentException("지정된 컬럼이 없습니다.");
        }
        builder.append("SELECT ")
                .append(String.join(", ", columns))
                .append(" FROM ")
                .append(table);
        if (alias != null) {
            builder.append(" AS ").append(alias);
        }
        join.assemble(builder);
        if (!condition.isEmpty()) {
            builder.append(" WHERE ");
            condition.assemble(builder);
        }
    }

    private static class Join implements Assemblable {
        private static final Join EMPTY = new Join("", "", null) {
            @Override
            public void assemble(StringBuilder builder) {
            }
        };

        private final String joinType;
        private final String table;
        private final JoinCondition condition;
        private String alias;

        public Join(String joinType, String table, JoinCondition condition) {
            this(joinType, table, condition, null);
        }

        public Join(String joinType, String table, JoinCondition condition, String alias) {
            this.joinType = joinType;
            this.table = table;
            this.condition = condition;
            this.alias = alias;
        }

        @Override
        public void assemble(StringBuilder builder) {
            builder.append(" ").append(joinType)
                    .append(" JOIN ")
                    .append(table);
            if (alias != null) {
                builder.append(" AS ").append(alias);
            }
            condition.assemble(builder);
        }
    }
}