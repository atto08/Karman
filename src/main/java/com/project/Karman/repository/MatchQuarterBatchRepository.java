package com.project.Karman.repository;

import com.project.Karman.dto.batch.MatchGoalBatchDto;
import com.project.Karman.dto.batch.MatchLineupBatchDto;
import com.project.Karman.dto.batch.PlayerStatBatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MatchQuarterBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    // 라인업 일괄 생성 (MatchLineup)
    public void batchInsertMatchLineup(List<MatchLineupBatchDto> lineups) {
        if (lineups.isEmpty()) return;

        // MatchPlayerInfo(@Embedded) -> name, affiliation_id 컬럼 매핑
        String sql = """
                INSERT INTO match_lineup (
                    match_lineup_id,
                    match_id,
                    quarter,
                    position_number,
                    club_player_position,
                    is_sub,
                    name,
                    affiliation_id,
                    created_at,
                    updated_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MatchLineupBatchDto lineup = lineups.get(i);
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                // 1. PK (UUID 직접 생성 확인)
                ps.setObject(1, lineup.matchLineupId());

                // 2. 복합키 FK (MatchQuarter)
                ps.setObject(2, lineup.matchId());
                ps.setInt(3, lineup.quarter());

                // 3. 일반 필드
                ps.setInt(4, lineup.positionNumber());
                ps.setString(5, lineup.position().name()); // Enum -> String
                ps.setBoolean(6, lineup.isSub());

                // 4. Embedded 필드 (PlayerInfo)
                ps.setString(7, lineup.playerInfo().getName());
                ps.setObject(8, lineup.playerInfo().getAffiliationId()); // UUID or Null

                // 5. BaseEntity 필드
                ps.setTimestamp(9, now);
                ps.setTimestamp(10, now);
            }

            @Override
            public int getBatchSize() {
                return lineups.size();
            }
        });
    }

    // 골 기록 일괄 생성 (MatchGoal)
    public void batchInsertMatchGoal(List<MatchGoalBatchDto> goals) {
        if (goals.isEmpty()) return;

        // @AttributeOverride 적용된 컬럼명 사용 (scorer_name, scorer_affiliation_id 등)
        String sql = """
                INSERT INTO match_goal (
                    match_goal_id,
                    match_id,
                    quarter,
                    scorer_name,
                    scorer_affiliation_id,
                    assist_name,
                    assist_affiliation_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MatchGoalBatchDto goal = goals.get(i);

                // 1. PK
                ps.setObject(1, goal.matchGoalId());

                // 2. 복합키 FK
                ps.setObject(2, goal.matchId());
                ps.setInt(3, goal.quarter());

                // 3. 득점자 정보 (@AttributeOverride: name -> scorer_name)
                ps.setString(4, goal.scorer().getName());
                ps.setObject(5, goal.scorer().getAffiliationId());

                // 4. 어시스트 정보 (NULL 체크 필수)
                if (goal.assist() != null && goal.assist().getName() != null) {
                    ps.setString(6, goal.assist().getName());
                    ps.setObject(7, goal.assist().getAffiliationId());
                } else {
                    ps.setObject(6, null);
                    ps.setObject(7, null);
                }
            }

            @Override
            public int getBatchSize() {
                return goals.size();
            }
        });
    }

    // 선수 스탯 증분 업데이트 (Affiliation)
    public void batchUpdateAffiliationStats(List<PlayerStatBatchDto> playerStats) {
        if (playerStats.isEmpty()) return;

        String sql = """
                UPDATE affiliation
                SET match_count = match_count + ?,
                    goal = goal + ?,
                    assist = assist + ?,
                    updated_at = ?
                WHERE affiliation_id = ?
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PlayerStatBatchDto delta = playerStats.get(i);
                UUID id = delta.affiliationId();
                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                ps.setLong(1, delta.matchCount()); // Long 타입 확인
                ps.setLong(2, delta.goal());
                ps.setLong(3, delta.assist());
                ps.setTimestamp(4, now);              // 수정일 업데이트
                ps.setObject(5, id);                  // WHERE 조건
            }

            @Override
            public int getBatchSize() {
                return playerStats.size();
            }
        });
    }
}
