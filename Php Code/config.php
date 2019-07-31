<?php
$servername = "localhost";
$username = "root";
$password = "tinga";
$database = "tinga_db";
 
// Create connection
$conn = mysqli_connect($servername, $username, $password, $database);
date_default_timezone_set('Asia/Kolkata');
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
//echo "Connected successfully";
