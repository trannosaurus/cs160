<?php

$db = new mysqli("127.0.0.1", "sjsucsor_s5g414s", "N0VACITY", "sjsucsor_160s5g42014s");
    
if ($db->connect_errno) {
    echo "Failed to connect to MySQL: " . $db->connect_error;
}
else {

    $stmt = $db->prepare("SELECT course_image, title, category, start_date, course_length, course_link, site, profname, profimage FROM course_data, coursedetails WHERE course_data.id = coursedetails.course_id");
    $result = $stmt->execute();
    $stmt->bind_result($course_image, $title, $category, $start_date, $course_length, $course_link, $site, $profname, $profimage);
    $course_data = "<tbody>";
                            
    while ($stmt->fetch()) {
        $course_data .= "<tr>";

        if (false === strpos($course_image, '://')) {
            $course_image = 'https://' . $course_image;
        }
        $course_data .= "<td><a href= '" . $course_link . "'><img width=200 height=150 src= '" . $course_image . "'></a></td>";
        $course_data .= "<td>" . $title . "</td>";
        $course_data .= "<td>" . $category . "</td>";
        $course_data .= "<td>" . $start_date . "</td>";
        $course_data .= "<td>" . $course_length . "</td>";
        $course_data .= "<td>" . $profname . "</td>";
        if (false === strpos($profimage, '://')) {
            $profimage = 'https://' . $profimage;
        }
        $course_data .= "<td><img width=100 height=100 src= '" . $profimage . "'></td>";
        $course_data .= "<td>" . $site . "</td>";
        $course_data .= "</tr>";
    }

    $course_data .= "</tbody>";
    $db->close();
    echo $course_data;
}  
   
?>
