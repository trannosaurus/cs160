<?php require('includes/db_connect.php');

session_start();

if (empty($_SESSION['username'])) {
	echo json_encode(null);
	return;
}

$statement = $db->prepare("SELECT course_id, title, rating, course_link
							 FROM course_data, course_rating
							 WHERE username = ? 
							 AND course_data.id = course_rating.course_id
							 ORDER BY title");

$statement->bind_param('s', $_SESSION['username']);
$result = $statement->execute();
$statement->bind_result($course_id, $title, $rating, $course_link);
$user_data = array (
	"ratedCourses" => array(),
	"wishlist" => array()
);

while ($statement->fetch()) {
    $user_data['ratedCourses'][$course_id] = array (
    	"title" => $title,
    	"rating" => $rating,
    	"link" => $course_link
    );
}

$statement = $db->prepare("SELECT course_id, title
						   FROM course_data, wishlist
						   WHERE username = ?
						   AND course_data.id = wishlist.course_id
						   ORDER BY title");

$statement->bind_param('s', $_SESSION['username']);
$result = $statement->execute();
$statement->bind_result($course_id, $title);

while ($statement->fetch()) {
    $user_data['wishlist'][$course_id] = $title;
}

$db->close();
$_SESSION['user_data'] = $user_data;
echo json_encode($user_data);

?> 
