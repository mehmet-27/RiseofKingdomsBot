package dev.mehmet27.rokbot.managers;


import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.configuration.file.FileConfiguration;
import dev.mehmet27.rokbot.peerlessscholar.QuestionNotFoundException;
import dev.mehmet27.rokbot.storage.DBCore;
import dev.mehmet27.rokbot.storage.MySQLCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class StorageManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private DBCore core = null;
	private final Main main;
	private final FileConfiguration config;

	public StorageManager(Main main) {
		this.main = main;
		this.config = main.getConfigManager().getConfig();
		try {
			if (config.getBoolean("mysql.enable", true)) {
				core = new MySQLCore("localhost", "makinist", 3306, "peerless", "p6VQ4wChKgyH1kF5");
			} else {
				//core = new SQLiteCore();
			}
		} catch (Exception e) {
			logger.error("An error occurred while connecting to the database.");
		}
		setup();
		//test();
	}

	public void test() {
		Path path = Paths.get(new File(Main.getInstance().getConfigManager().getDataFolder() + File.separator + "test").toURI());

		String test;
		try {
			test = Files.readString(path, StandardCharsets.UTF_8);
			Map<String, String> map = new LinkedHashMap<>();
			String[] lines = test.split("\\r?\\n");

			for (int i = 0; i < lines.length - 1; i += 2) {
				map.put(lines[i], lines[i + 1]);
			}

			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
				insertQuestionWithAnswer(entry.getKey(), entry.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setup() {
        /*core.execute("CREATE TABLE IF NOT EXISTS `mrank_profiles` (`uuid` VARCHAR(255), `rank` VARCHAR(255), PRIMARY KEY (uuid))");
        String query = "CREATE TABLE IF NOT EXISTS mrank_progress (" +
                " uuid VARCHAR(255) NOT NULL," +
                " requirement_id VARCHAR(255) NOT NULL," +
                " progress VARCHAR(255) NOT NULL DEFAULT '0')";
        core.execute(query);*/
	}

	public Map<String, Integer> getPlayerProgress(UUID uuid) {
		Map<String, Integer> progressMap = new HashMap<>();
		String query = String.format("SELECT * FROM mrank_progress WHERE uuid = '%s'", uuid.toString());
		try (Connection connection = core.getSource().getConnection()) {
			ResultSet result = connection.createStatement().executeQuery(query);
			while (result.next()) {
				String requirementId = result.getString("requirement_id");
				int progress = result.getInt("progress");
				progressMap.put(requirementId, progress);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return progressMap;
	}

    /*public Profile getProfile(UUID uuid) {
        String rankName = getRankByUuid(uuid);
        return new Profile(uuid, RankUtils.getRankByName(rankName));
    }*/

	public String getQuestionAnswer(String question) throws QuestionNotFoundException {
		String query = "SELECT answer FROM `peerless_scholar` WHERE question=?";
		try (Connection connection = core.getSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, question);
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				return result.getString("answer");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new QuestionNotFoundException("Searched question not found in database.");
	}

	public void insertQuestion(String question) {
		String query = "INSERT INTO `peerless_scholar` (question) VALUES (?)";
		try (Connection connection = core.getSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, question);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertQuestionWithAnswer(String question, String answer) {
		String query = "INSERT INTO `peerless_scholar` (question,answer) VALUES (?,?)";
		try (Connection connection = core.getSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, question);
			ps.setString(2, answer);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /*public void updateProfile(Profile profile) {
        String query = "UPDATE `mrank_profiles` SET `rank` = ? WHERE `uuid` = ?";
        try (Connection connection = core.getSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, profile.getRank().getName());
            ps.setString(2, profile.getUuid().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProgress(Profile profile) {
        for (Map.Entry<String, Integer> entry : profile.getProgress().entrySet()) {
            String query = "INSERT INTO `mrank_progress` (uuid,requirement_id,progress) VALUES (?,?,?)";
            try (Connection connection = core.getSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, profile.getUuid().toString());
                ps.setString(2, entry.getKey());
                ps.setInt(3, entry.getValue());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/

	public void deletePlayerProgress(UUID uuid) {
		String query = String.format("DELETE FROM mrank_progress WHERE uuid = '%s'",
				uuid);
		core.execute(query);
	}

    /*public void createPlayer(UUID uuid) {
        String query = "INSERT INTO `mrank_profiles` (uuid,rank) VALUES (?,?)";
        try (Connection connection = core.getSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, RankUtils.getDefaultRank().clone().getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning(String.format("An error occurred while loading %s's profile.", uuid));
            e.printStackTrace();
        }
    }*/

	public boolean isPlayerExists(UUID uuid) {
		String query = "SELECT * FROM `mrank_profiles` WHERE uuid=?";
		try (Connection connection = core.getSource().getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, uuid.toString());
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public DBCore getCore() {
		return core;
	}
}
