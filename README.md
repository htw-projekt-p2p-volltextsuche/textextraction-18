<h1>Textextraktion-18</h1>
The content-related task of the text extraction-18 was to extract all speeches of the 1st - 18th legislative period,
the XML documents passed by the crawler and output them in JSON files.
files. The crawler achieved this by temporarily persisting the plenary minutes as
as XML files and passing them as parameters to the text extraction-18.<br><br>

The basic idea is to examine the text block semantically and syntactically in order to find a search algorithm.
algorithm, which will find the text within the text block:<br>
&#9679; Title of the speech &emsp; &#9679; Name of the speaker &emsp; &#9679; Affiliation &emsp; &#9679; Date of the speech &emsp; &#9679; Speech<br>
to a person and extracts it.<br><br>
<hr>
<h3>Problems</h3>
Most of the problems arose from the assumption that there was consistency in the formatting of the documents.
<table>
	<tr>
		<th style="text-align:left">Problem</th>
		<th style="text-align:left">Description</th>
	</tr>
	<tr>
		<td>Document type</td>
		<td>In the 1st - 14th electoral legislature period, a document type was given to the XML files, but from the from the 15th electoral legislature onwards, this was no longer the case.</td>
	</tr>
	<tr>
		<td>Formatting</td>
		<td>In some documents, the considered commonalities of the table of contents were also not present, which makes a uniform programme for all electoral legislatures almost impossible.</td>
	</tr>
	<tr>
		<td>Naming of the affiliation</td>
		<td>There were also many spelling differences in the naming of affiliations. The best example of this is the party "BUNDNIS 90/DIE GRÜNEN". For the different spellings of this party alone:
		<ul>
			<li>BÜNDNIS 90/DIE GRÜNEN</li>
			<li>BÜNDNIS 90/<br>DIE GRÜNEN</li>
			<li>BÜNDNIS 90<br><br>/DIE GRÜNEN</li>
			<li>BÜNDNIS 90/DIE GRÜ-<br>NEN</li>
		</ul>
three regular printouts had to be changed to make them work.</td>
	</tr>
</table>
The decisive point in the extraction was the search in the table of contents for persons. Within the search itself, however, there were differences that had to be processed separately. The search was implemented with the method createMap() in the SpeechSearch class. The literal flow of the method is roughly as follows:
<dl>
	<dt><b>If</b> there is a title</dt>
	<dd><b>then</b> the first name is also contained in the same entry and must be saved<br>after that, each entry is a name,<br>until the entry that contains a title.<br>Next loop pass from this title.</dd>
	<dt><b>otherwise</b></dt>
	<dd>
		<dl>
			<dt><b>if</b> a title is followed by a name</dt>
			<dd><b>then</b> each subsequent entry is a name and must be saved,<br>until an entry contains a title.<br>next loop pass from this title.</dd>
		</dl>
	</dd>
</dl>
If the speakers were found for each title, these entries are saved in a map.

<hr>
<h3>Build Jar File</h3>
<code>mvn clean package</code>

<hr>
<h3>Execute</h3>
<code>java -jar textextraction-18.jar</code><br>
and all documents to be extracted as transfer parameters in the console
