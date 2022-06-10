<?php
// Class For DataBase Connection Managenent;
require_once '../db_config.php';
class MY_SQLDB {
	private $db_server_name = DB_HOST;
	private $db_user_name = DB_USER;
	private $db_password = DB_PASS;
	private $db_name = DB_NAME;


	private $data = array();

	public function get_connection() {
		$this->connection = mysqli_connect($this->db_server_name, $this->db_user_name, $this->db_password, $this->db_name);

		if (!$this->connection) {
			die("Connection failed: " . mysqli_connect_error());
		}
		
		return $this->connection;
	}

	public function get_rows($sql) {

		if (!property_exists($this, 'connection')) {
			$this->get_connection();
		}
		$this->result = mysqli_query($this->connection, $sql);
		while($row = mysqli_fetch_array($this->result, MYSQLI_NUM)) {
			array_push($this->data, $row);
		}

		return $this->data;
	}

	public function get_column_names() {
		if (property_exists($this, 'result')) {
			function extract_column_names($val) {
				return $val->name;
			}

			$this->columns = array_map("extract_column_names", mysqli_fetch_fields($this->result));
			return $this->columns;
		}
	}


	public function close_connection() {
		mysqli_free_result($this->result);
		mysqli_close($this->connection);
	}


}

?>